<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<#-- <link rel="stylesheet" href="${request.contextPath}/css/jquery.treegrid.css"> 
<link rel="stylesheet" href="${request.contextPath}/css/jquery-ui.min.css">
<link rel="stylesheet" href="${request.contextPath}/css/ui.jqgrid.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/treegrid/themes/default/easyui.css">
<link rel="stylesheet" type="text/css" href="${request.contextPath}/treegrid/themes/icon.css">-->
<#include "/common/include.ftl" />
<#-- <script type="text/javascript" src="${request.contextPath}/js/jquery.treegrid.js"></script>
<script type="text/javascript" src="${request.contextPath}/treegrid/jquery.easyui.min.js"></script>
<script type="text/javascript" src="${request.contextPath}/treegrid/easyui-lang-zh_CN.js"></script> -->
<title>流向列表</title>
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
.form-group{
	padding:5px 5px 0px 0px;
}
/* .panel{
	margin:0;
}
.panel-body{
	padding:0;
}
.panel-header, .panel-body{
	border-color:#ddd;
}
.datagrid-toolbar, .datagrid-pager{
	background:white;
}
.datagrid-header-row, .datagrid-row{
	height:35px;
}
.tree-folder-open, .tree-folder{
	
} */
</style>
</head>
<body>
	<div style="display: black;padding:10px 0px 0px 10px; margin-top:5px;" id="searchDiv">
		<div class="clearfix">
		<form class="form-inline" role="form" id="relationForm">
			<div class="form-group">
				<font>流向名称:</font>
				<input id="relationName" name="relationName"  type="text" style="width:200px;" placeholder="输入流向名称"  />
			</div>
			<div class="form-group form-group-select2">
				<font>源服务类型:</font>
				<select class="" id="srcServerType" name="srcServerType"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group form-group-select2">
				<font>源服务:</font>
				<#-- <input id="srcServerName" name="srcServerName"  type="text" style="width:150px;" placeholder="源服务名称"  /> -->
				<select class="" id="srcServerId" name="srcServerId" style="width: 180px;margin-left: 15px;">
					
				</select>
			</div>
			<div class="form-group">
				<font>源服务功能码:</font>
				<input id="srcConsumerFc" name="srcConsumerFc"  type="text" style="width:150px;" placeholder="源服务功能码"  />
			</div>
			<div class="form-group form-group-select2">
				<font>目标服务类型:</font>
				<select class="" id="destServerType" name="destServerType"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group form-group-select2">
				<font>目标服务:</font>
				<#-- <input id="destServerName" name="destServerName" type="text" style="width:150px;" placeholder="目标服务名称"  /> -->
				<select class="" id="destServerId" name="destServerId" style="width: 180px;margin-left: 15px;">
					
				</select>
			</div>
			<div class="form-group">
				<font>目标服务功能码:</font>
				<input id="destProviderFc" name="destProviderFc"  type="text" style="width:150px;" placeholder="目标服务功能码"  />
			</div>
			<div class="form-group form-group-select2">
				<font>协议:</font>
				<select class="" id="destProtocol" name="destProtocol"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" id="search-btn" onclick="search()">
					<i class="glyphicon glyphicon-search"></i> 搜索 
				</button>
			</div>
		</form>
		</div>
	</div>
	<div style="display: none;padding:10px 0px 0px 10px; margin-top:5px;" id="btns">
		<div class="clearfix">
		<form class="form-inline" role="form" id="relationForm">
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="editForm()">
					<i class="fa  fa-plus"></i> 新增 
				</button>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" onclick="validateRelation()">
					<i class=""></i> 校验流向 
				</button>
			</div>	
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" id="export" onclick="exportRelation()">
					<i class=""></i> 导出列表 
				</button>
			</div>	
			<div class="form-group">
				<button class="btn btn-danger" type="button" onclick="batchDelete()">
					<i class="fa fa-trash-o"></i> 删除 
				</button>
			</div>
		</form>
		</div>
	</div>
	<div class="main-box clearfix" style="margin-bottom: 0px;">
	   <div class="jqGrid_wrapper">
           <table id="relationTree"></table>
    	   <div id="gridPager"></div>
       </div>
	</div>
	
	<div id="validating" style="">
		<font size="14">正在校验...</font>
		<img height="24" alt="正在校验..." src="${request.contextPath}/img/select2-spinner.gif">
	</div>
	
</body>
<script type="text/javascript">

	$(function(){
		
		serverTypeSelect();
		protocolSelect("destProtocol", "全部");
		serverSelect();
		
		$.jgrid.defaults.styleUI="Bootstrap";
		$("#relationTree").jqGrid({
			url:"${request.contextPath}/relation/allRelation",
			postData : {"relationName" : $("#sRelationName").val()},
			datatype:"json",
			height: window.innerHeight-heights-110,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: true,//复选框   
			rownumbers: false,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["流向名称","源服务","源服务ID","消费功能码","目标服务","目标服务ID","目标功能码","操作"],
            colModel:[
              { name:"relationName",index:"relationName",hidden:false},
              { name:"srcServerName",index:"srcServerName",hidden:false},
              { name:"srcServerId",index:"srcServerId",hidden:false},
              { name:"srcConsumerFc",index:"srcConsumerFc",hidden:false},
              { name:"destServerName",index:"destServerName",hidden:false},
              { name:"destServerId",index:"destServerId",hidden:false},
              { name:"destProviderFc",index:"destProviderFc",hidden:false},
              { name:"id",index:"id",hidden:false,formatter:function(cellvalue, options, rowdata){
            	  var html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
        		  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
        		  return html;
              }}
            ], 
			pager:"#gridPager",
			viewrecords:true,
			caption:"流向列表",
			hidegrid:false
		}).jqGrid('navGrid','#gridPager',{refresh:true,add:false,edit:false,del:false,search:false});
		
		
		/* $.jgrid.defaults.styleUI="Bootstrap";
		var url = "${request.contextPath}/relation/tree";
		$("#relationTree").treegrid({
			url : url,
			pagination: true,
			width: "auto",
			height: window.innerHeight-85,
			pageSize: 10,
			pageList: [10,20,30],
			idField : 'id',
			treeField : 'relationName',
			columns : [ [ {
				field : 'relationName',
				title : '名称',
				align : 'left',
				width : "25%",
				formatter : function(value, row, index) {
					if(value==null){
						return row.id;
					}
					return value;
				}
			},
			{
				field : 'srcConsumerFc',
				title : '消费功能码',
				align : 'left',
				width : "12.5%"
			},
			{
				field : 'destServerName',
				title : '目标服务',
				align : 'left',
				width : "15%"
			},
			{
				field : 'destServerId',
				title : '目标服务ID',
				align : 'left',
				width : "20%"
			},
			{
				field : 'destProviderFc',
				title : '目标功能码',
				align : 'left',
				width : "12.5%"
			},
			{
				field : 'id',
				title : '操作',
				align : 'left',
				width : "15%",
				formatter : function(value, row, index) {
					var html = "";
					if(row.pid != null && row.children.length > 0){
						html = '<a href="#" class="table-link" title="删除" onclick="delByServerId(\''+value+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
					}
					if(row.pid != null && row.children.length <= 0){
						 html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+value+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
		        		 html+='&nbsp;<a href="#" class="table-link" title="删除" onclick="delForm(\''+value+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
					}
					return html;
				}
			   } 
			] ],
			onLoadSuccess : function(e,row) {
				$("#relationTree").treegrid('expandAll');
			},
			onBeforeExpand : function(row){
				
			},
			
		}); */
		 
		$(".ui-jqgrid-caption").html($("#btns").html());
		$(window).bind("resize",function(){
			var width=$(".jqGrid_wrapper").width();
			var height = window.innerHeight-110;
			$("#relationTree").setGridWidth(width);
			$("#relationTree").setGridHeight(height);
			/*$('#relationTree').treegrid('resize', {
				width:width,
				height:height
			});*/
		});
	});
		
	function editForm(id){
		var title="新增流向";
		var url="${request.contextPath}/relation/editPage";
		if(id!=undefined){
			title="编辑流向";
			url="${request.contextPath}/relation/editPage?id="+id;
		}
		parent.openwin(title,url, 500, 700, function(){
			//$("#relationTree").treegrid("reload");
			$("#relationTree").trigger("reloadGrid");
		});
	}
	
	function delForm(id){
		parent.confirms("确定删除？",function(){
			var url="${request.contextPath}/relation/delete";
			$.ajax({
				type:"post",
				url : url,
				data:{"id" : id},
				async:false,
				success : function(data) {
					if(data.success){
						parent.opentip("删除成功！");
						$("#relationTree").trigger("reloadGrid");
					//	$("#relationTree").treegrid("reload");
					}else{
						parent.opentip("删除失败！");
					}
				}
			});
		});
		
	}
	
	function delByServerId(serverId){
		parent.confirms("确定删除此服务的所有流向？",function(){
			var url="${request.contextPath}/relation/delByServerId";
			$.ajax({
				type:"post",
				url : url,
				data:{"serverId" : serverId},
				async:false,
				success : function(data) {
					if(data.success){
						parent.opentip("删除成功！");
						$("#relationTree").treegrid("reload");
					}else{
						parent.opentip("删除失败！");
					}
				}
			});
		});
	}
	
	function search(){
		console.log($('#relationForm').serialize());
		var url = "${request.contextPath}/relation/allRelation?"+$('#relationForm').serialize();
		$("#relationTree").jqGrid('setGridParam',{url: url,postData : {},page:1}).trigger("reloadGrid");
	//	$("#relationTree").treegrid('reload', {"relationName" : $("#sRelationName").val()});
	}
	
	function validateRelation(){
		var url="${request.contextPath}/relation/validate";
		$("#validating").show();
		$.ajax({
			type:"post",
			url : url,
			data:{},
			async:true,
			success : function(data) {
				$("#validating").hide();
				if(data.success){
					parent.opentip("校验成功！");
				}else{
					parent.opentip("校验失败！");
				}
			},
			error : function(){
				$("#validating").hide();
				parent.opentip("校验失败！");
			}
		});
	}
	
	function exportRelation(){
		window.location.href = "${request.contextPath}/relation/exportRelation";
	}
	
	function importRelation(){
		var url="${request.contextPath}/relation/import";
		parent.openwin("上传文件",url, 500, 200, function(){
			$("#relationTree").treegrid("reload");
		});
	}
	
	function serverTypeSelect(){
		var optionHtml = "<option value=''>全部</option>";
		 $.ajax({
			type:"post",
			url : '${request.contextPath}/serverType/getNoUseType',
			data:{},
			async: false,
			success : function(data) {
				 $.each(data,function(i,item){
					 //typeNameMap[item.serverType] = item.serverTypeName;
					 optionHtml+="<option value="+item.serverType+">"+item.serverTypeName+"</option>";
				 });
			}
		});
		
		$("#srcServerType").html(optionHtml);
		$("#srcServerType").select2();
		
		$("#destServerType").html(optionHtml);
		$("#destServerType").select2();
	}
	
	function protocolSelect(id, tip){
		var optionHtml = "<option value=''>"+tip+"</option>";
		 $.ajax({
			type:"post",
			url:"${request.contextPath}/agreement/getAllTypePage",
			data:{page:1, rows:1000},
			async: false,
			success : function(data) {
				 datas = data["rows"];
				 $.each(datas,function(i,item){
					 //typeNameMap[item.serverType] = item.serverTypeName;
					 optionHtml+="<option value="+item.type+">"+item.type+"</option>";
				 });
			}
		});
		$("#"+id).html(optionHtml);
		$("#"+id).select2();
	}
	
	function batchDelete(){
		var ids = $("#relationTree").jqGrid('getGridParam','selarrrow');
		if(ids.length < 1){
			parent.opentip("请选择需要删除的流向！");
			return;
		}
		parent.confirms("确定删除？",function(){
			var url="${request.contextPath}/relation/batchDelete";
			$.ajax({
				type:"post",
				url : url,
				data:{"ids":JSON.stringify(ids)},
				success : function(data) {
					if(data.success){
						parent.opentip("删除成功！");
					}else{
						parent.opentip("删除失败！");
					}
					$("#relationTree").trigger("reloadGrid");
				}
			});
		});
	}
	
	function serverSelect(){
		var optionHtml = "<option value=''>全部</option>";
		$.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/getServer',
			async: false,
			success : function(data) {
				 $.each(data,function(i,item){
					 optionHtml+="<option value="+item.id+">"+item.serverName+"</option>";
				 });
			}
		}); 
		 
		$("#srcServerId").html(optionHtml);
		$("#srcServerId").select2();
		
		$("#destServerId").html(optionHtml);
		$("#destServerId").select2();
	}
</script>
</html>