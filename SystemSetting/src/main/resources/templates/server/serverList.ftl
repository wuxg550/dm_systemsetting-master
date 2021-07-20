<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务管理列表</title>
<#include "/common/include.ftl" /> 
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px;">
	   <div class="jqGrid_wrapper">
           <table id="tableData"></table>
           <div id="tablePager"></div>
       </div>
	</div>
	<div style="display: none;" id="btns">
		<#-- <button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="editForm()">
			<i class="fa  fa-plus"></i> 新增 
		</button> -->
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$.jgrid.defaults.styleUI="Bootstrap";
		$("#tableData").jqGrid({
			url:"${request.contextPath}/getAllPage",
			datatype:"json",
			height: window.innerHeight-heights,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: false,//复选框   
			rownumbers: true,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["主机","端口","配置","Server状态","操作"],
			colModel:[
			          {name:"ip",index:"ip",width:100,sortable:false},
			          {name:"port",index:"port",width:70,sortable:false},
			          {name:"path",index:"path",width:120},
			          {name:"status",index:"status",width:70,formatter:function(cellvalue, options, rowdata){
				    	 return '';
						 if(cellvalue=='ONLINE'){
				    		 return '<span class="label label-success">在线</span>';
				    	 }else if(cellvalue=='OFFLINE'){
				    		 return '<span class="label label-danger">离线</span>';
				    	 }else{
				    		 return '<span class="label label-warning">异常</span>';
				    	 }
		          	  }},
				      {name:"id",index:"id",width:150,sortable:false,formatter:function(cellvalue, options, rowdata){
				    	  var html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
				    	// 	  if(rowdata.status=='ONLINE'){
				    	// 		 html+='<a href="#" class="table-link" title="初始化节点" onclick="initNode(\''+rowdata.ip+'\',\''+rowdata.port+'\')"><span class="label label-primary"><i class="fa fa-recycle"></i> 初始化节点</span></a>';
				    	// 	  }
			        	//	  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
			        	return html;
			          }}
			       ],
			pager:"#tablePager",
			viewrecords:true,
			caption:"服务列表",
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
		var title="新增服务";
		var url="${request.contextPath}/serverEdit";
		if(id!=undefined){
			title="编辑服务";
			url="${request.contextPath}/serverEdit?id="+id;
		}
		parent.openwin(title,url, 600, 400, function(){
			 $("#tableData").trigger("reloadGrid");
		});
	}
	
	function delForm(id){
		parent.confirms("您确定要删除该服务？",function(){
			 $.ajax({
				type:"get",
				url : '${request.contextPath}/delServer/'+id,
				success : function(data) {
					 parent.opentip("删除成功！");
					 $("#tableData").trigger("reloadGrid");
				}
			}); 
		});
	}
	
	//初始化节点
	function initNode(ip,port){
		$.ajax({
			type:"get",
			url : '${request.contextPath}/initNode',
			data:{ip:ip,port:port},
			success : function(data) {
				 parent.opentip("节点初始化成功！");
			}
		});
	}
	</script>
</body>
</html>