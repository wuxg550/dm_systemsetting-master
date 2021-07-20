<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<link rel="stylesheet" href="${request.contextPath}/css/jquery.treegrid.css">
<#-- <link rel="stylesheet" href="${request.contextPath}/css/jquery-ui.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/ui.jqgrid.css"> -->
<link rel="stylesheet" type="text/css" href="${request.contextPath}/treegrid/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/treegrid/themes/icon.css">
<#include "/common/include.ftl" />
<script type="text/javascript" src="${request.contextPath}/js/jquery.treegrid.js"></script>
<script type="text/javascript" src="${request.contextPath}/treegrid/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/treegrid/easyui-lang-zh_CN.js"></script>
<title>流向模板列表</title>
<style type="text/css">
#validating{
	display: none;
	text-align:center;
	position:absolute;
	top:0px;
	left:0px;
	width:100%;
	heith:100%;
	margin-top:25%;
	z-index:9
}
</style>
</head>
<body>
	<div style="display: black;padding:10px 0px 0px 10px;">
		<div class="clearfix">
		<form class="form-inline" role="form" id="templetForm">
			<div class="form-group form-group-select2">
				<font>源服务类型:</font>
				<select class="" id="srcServerType" name="srcServerType"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group form-group-select2">
				<font>源服务功能码:</font>
				<input id="srcConsumerFc" name="srcConsumerFc" type="text" style="width:150px;"/>
			</div>
			<div class="form-group form-group-select2">
				<font>目标服务类型:</font>
				<select class="" id="destServerType" name="destServerType"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group form-group-select2">
				<font>目标服务功能码:</font>
				<input id="destProviderFc" name="destProviderFc" type="text" style="width:150px;"/>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" id="search-btn" onclick="search()">
					<i class="glyphicon glyphicon-search"></i> 搜索 
				</button>
			</div>
				
		</form>
		</div>
	</div>
	<div style="display: none;" id="btns">
			<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="editForm()">
				<i class="fa  fa-plus"></i> 新增 
			</button>
			<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="downExcel()">
				<i class=""></i> 下载excel模板 
			</button>
			<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="openImportPage()">
				<i class=""></i> 导入 
			</button>
			<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="exportTemplet()">
				<i class=""></i> 导出 
			</button>
	</div>
	<div class="main-box clearfix" style="margin-bottom: 0px;">
	   <div class="jqGrid_wrapper">
           <table id="templateTb"></table>
    	   <div id="gridPager"></div>
       </div>
	</div>
	
</body>
<script type="text/javascript">
	
	var typeNameMap = new Array();
	
	function getTypeData(){
		$.ajax({
			type:"post",
			url : '${request.contextPath}/serverType/getNoUseType',
			data:{},
			async: false,
			success : function(data) {
				 $.each(data,function(i,item){
					 typeNameMap[item.serverType] = item.serverTypeName;
					 
				 });
			}
		});
	}
	
	function iniTypeSelect(id, tip){
		var optionHtml = "<option value=''>"+tip+"</option>";
		for(var i in typeNameMap){
			optionHtml+="<option value="+i+">"+typeNameMap[i]+"</option>"; 
		}
		$("#"+id).html(optionHtml);
		$("#"+id).select2();
	}
	
	$(function(){
		getTypeData();
		iniTypeSelect("srcServerType", "全部");
		iniTypeSelect("destServerType", "全部");
		
		$.jgrid.defaults.styleUI="Bootstrap";
		$("#templateTb").jqGrid({
			url:"${request.contextPath}/relation/templet/list",
			postData : {},
			datatype:"json",
			height: window.innerHeight-heights-50,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: false,//复选框   
			rownumbers: false,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["源服务","消费功能码","目标服务","目标功能码","描述","操作"],
            colModel:[
              { name:"srcServerType",index:"srcServerName",hidden:false,
            	  formatter:function(cellvalue, options, rowdata){
					  return typeNameMap[cellvalue];
            	  }
              },
              { name:"srcConsumerFc",index:"srcConsumerFc",hidden:false},
              { name:"destServerType",index:"destServerName",hidden:false,
            	  formatter:function(cellvalue, options, rowdata){
            		  return typeNameMap[cellvalue];
            	  }
              },
              { name:"destProviderFc",index:"destProviderFc",hidden:false},
			  { name:"description",index:"description",hidden:false},
              { name:"id",index:"id",hidden:false,formatter:function(cellvalue, options, rowdata){
            	  var html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
        		  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
        		  return html;
              }}
            ], 
			pager:"#gridPager",
			viewrecords:true,
			caption:"模板列表",
			hidegrid:false
		}).jqGrid('navGrid','#gridPager',{refresh:true,add:false,edit:false,del:false,search:false});
	
		$(".ui-jqgrid-caption").html($("#btns").html());
//        $(".ui-jqgrid-caption").hide();
		$(window).bind("resize",function(){
			var width=$(".jqGrid_wrapper").width();
			$("#templateTb").setGridWidth(width);
			$("#templateTb").setGridHeight(window.innerHeight-heights-50);
		});
	});
	
	function editForm(id){
		var title="新增模板";
		var url="${request.contextPath}/relation/templet/editPage";
		if(id!=undefined){
			title="编辑模板";
			url="${request.contextPath}/relation/templet/editPage?id="+id;
		}
		parent.openwin(title,url, 500, 500, function(){
			$("#templateTb").trigger("reloadGrid");
		});
	}
	
	function delForm(id){
		parent.confirms("确定删除？",function(){
			var url="${request.contextPath}/relation/templet/delete";
			$.ajax({
				type:"post",
				url : url,
				data:{"id" : id},
				async:false,
				success : function(data) {
					if(data.success){
						parent.opentip("删除成功！");
						$("#templateTb").trigger("reloadGrid");
					}else{
						parent.opentip("删除失败！");
					}
				}
			});
		});
		
	}
	
	function search(){
		var url = "${request.contextPath}/relation/templet/list?"+$('#templetForm').serialize();
		$("#templateTb").jqGrid('setGridParam',{url: url,postData : {},page:1}).trigger("reloadGrid");
	}
	
	function downExcel(){
		var url = "${request.contextPath}/relation/templet/downloadExcelTemplet";
		window.location.href = url;
	}
	
	function openImportPage(){
		var url="${request.contextPath}/relation/templet/uploadPage";
		parent.openwin("导入流向模板",url, 500, 200, function(){
			$("#templateTb").treegrid("reload");
		});
	}
	
	function exportTemplet(){
		var url = "${request.contextPath}/relation/templet/downloadExcel?"+$('#templetForm').serialize();
		window.location.href = url;
	}
</script>
</html>