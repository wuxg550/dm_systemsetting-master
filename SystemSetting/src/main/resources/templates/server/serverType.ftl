<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务管理列表</title><#include "/common/include.ftl" /> 
</head>
<body>
	
	<div style="display: black;padding:10px 0px 0px 10px; margin-top:5px;" id="searchDiv">
		<div class="clearfix">
		<form class="form-inline" role="form" id="serverForm">
			<div class="form-group">
				<font>服务类型:</font>
				<input class="form-control" id="serverType" name="serverType"  type="text" style="width:150px;" placeholder="服务类型"  />
			</div>
			<div class="form-group">
				<font>服务名称:</font>
				<input class="form-control" id="serverTypeName" name="serverTypeName"  type="text" style="width:150px;" placeholder="服务类型名称"  />
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" id="search-btn" onclick="search()">
					<i class="glyphicon glyphicon-search"></i> 搜索 
				</button>
			</div>
		</form>
		</div>
	</div>

	<div class="main-box clearfix" style="margin-bottom: 0px;">
	   <div class="jqGrid_wrapper">
           <table id="tableData"></table>
           <div id="tablePager"></div>
       </div>
	</div>
	<div style="display: none;" id="btns">
		<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="editForm()">
			<i class="fa  fa-plus"></i> 新增 
		</button>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$.jgrid.defaults.styleUI="Bootstrap";
		$("#tableData").jqGrid({
			url:"${request.contextPath}/serverType/getAllTypePage",
			datatype:"json",
			height: window.innerHeight-heights-55,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: false,//复选框   
			rownumbers: true,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["服务类型","服务类型名称","服务说明","操作"],
			colModel:[
			          {name:"serverType",index:"serverType",width:100,sortable:false},
			          {name:"serverTypeName",index:"serverTypeName",width:120},
			          {name:"serverRemark",index:"serverRemark",width:70,},
				      {name:"id",index:"id",width:150,sortable:false,formatter:function(cellvalue, options, rowdata){
				    	  var html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
			        		  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
			        		  return html;
			          }}
			       ],
			pager:"#tablePager",
			viewrecords:true,
			caption:"服务类型列表",
			hidegrid:false
			}).jqGrid('navGrid','#tablePager',{refresh:true,add:false,edit:false,del:false,search:false});
		 $(".ui-jqgrid-caption").html($("#btns").html());
		 $(window).bind("resize",function(){
			var width=$(".jqGrid_wrapper").width();
			$("#tableData").setGridWidth(width);
			$("#tableData").setGridHeight(window.innerHeight-heights);
		 });
	});
	
	function editForm(id){
		var title="新增服务类型";
		var url="${request.contextPath}/serverType/serverTypeEdit";
		if(id!=undefined){
			title="编辑服务类型";
			url="${request.contextPath}/serverType/updataServerType?id="+id;
		}
		parent.openwin(title,url, 600, 500, function(){
			 $("#tableData").trigger("reloadGrid");
		});
	}
	
	function delForm(id){
		parent.confirms("您确定要删除该服务类型？",function(){
			 $.ajax({
				type:"post",
				url : '${request.contextPath}/serverType/deletType',
				data:{id:id},
				success : function(data) {
					 parent.opentip("删除成功！");
					 $("#tableData").trigger("reloadGrid");
				}
			}); 
		});
	}
	
	function search(){
		var url = "${request.contextPath}/serverType/getAllTypePage?"+$('#serverForm').serialize();
		$("#tableData").jqGrid('setGridParam',{url: url,postData : {},page:1}).trigger("reloadGrid");
	}
	</script>
</body>
</html>