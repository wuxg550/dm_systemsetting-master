<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>节点树管理</title><#include "/common/include.ftl" /> 
<link href="${request.contextPath}/js/plugins/jsTree/themes/default/style.min.css" rel="stylesheet">
<script src="${request.contextPath}/js/plugins/jsTree/jstree.min.js"></script>
<style type="text/css">
body,html{height:99%;}
  #theme-wrapper {
  box-shadow:none;
  padding: 10px;
  }
  .clearfix {
  margin-bottom: 7px;
  padding-top: 11px;
  }
  .tableCol {
   padding-left: 1px;
  }
  #treeDiv {overflow: auto;padding-left: 8px;}
  .font_size{
  font-size: 1.4em;
}

#xiangqing{
	display: none;
	text-align:center;
	position:absolute;
	top:0px;
	left:0px;
	width:60%;
	heith:80%;
	margin-top:18%;
	margin-left:25%;
	z-index:9;
	padding: 0px 10px 25px 0px;
}
</style>
</head>
<body style="background-color: #f1f3f5;">

<div id="xiangqing" style="background-color: #2980b9;">
	<div style="text-align: right;top: 0px;left: 80%;">
		<div class="form-group">
			<div class="col-lg-offset-2 col-lg-10" style="padding-bottom: 10px;">
				<button type="button" class="" onclick="closesDetails()">X</button>
			</div>
		</div>
	</div>
	<div class="layui-layer-content">
		<div class="form-group">
			<label for="dataPath" class="col-lg-2 control-label">节点路径：</label>
			<div class="col-lg-10">
				<input type="text" class="form-control" id="dataPath">
			</div>
		</div>
		<div class="form-group">
			<label for="dataArea" class="col-lg-2 control-label" id="dataType">节点数据：</label>
			<div class="col-lg-10">
				<textarea class="form-control" id="dataArea" rows="8"></textarea>
			</div>
		</div>
	</div>
</div>

<div id="theme-wrapper">
	<div class="row" >
		<div class="col-lg-12">
		<div class="main-box clearfix" id="serverlist"></div>
		</div>
	</div>
	<div class="row">
		<div class="col-lg-3">
			<div class="main-box clearfix" id="treeDiv">
			   <div id="treeData" class="tree-demo"></div>
			</div>
		</div>
		<div class="col-lg-9 tableCol">
			<div class="main-box clearfix">
				 <div class="jqGrid_wrapper">
	           		<table id="tableData"></table>
	           		<div id="tablePager"></div>
	      		 </div>
			</div>
		</div>
	</div>
	<div style="display: none;" id="btns">
		<!-- <button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="editForm()">
			<i class="fa  fa-plus"></i> 新增 
		</button> -->
		<!-- <button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="validateData()">
			<i class=""></i> 同步数据
		</button>
		 -->
	</div>
	<script type="text/javascript">
	var ip = "";
	var port = "";
	$(document).ready(function(){
		initZkServer();//初始zookeeper服务列表
		initDataTable();//
	});
	
	//初始zookeeper服务列表
	function initZkServer(){
		 $.ajax({
				type:'post',
				data:'',
				url:'${request.contextPath}/node/findAllZkServer',
				async:false,
				success:function(data){
					var html = "";
					$.each(data,function(i,item){
						var classType = "";
						if(item.status == 'ONLINE'){
							classType = "green-bg";
						} else if (item.status == 'OFFLINE'){
							classType = "red-bg";
						} else {
							classType = "red-bg";
						}
					  html += '<div class="col-md-3 col-sm-4 col-xs-10" style="cursor: pointer;" onclick="getServerTree(this)">'+
							 '<div class="main-box small-graph-box '+classType+'">'+
							 '<span class="subinfo">'+
								'<span class="social-count font_size">ip:</span>'+
								'<span class="social-action font_left font_size" name="ip">'+item.ip+'</span>'+
								'<span class="social-action font_left font_size" name="status" style="display:none;">'+item.status+'</span>'+
							 '</span>'+
							 '<span class="subinfo">'+
								'<span class="social-count font_size">端口:</span>'+
								'<span class="social-action font_left font_size" name="port">'+item.port+'</span>'+
							' </span>'+
							 '</div>'+
							 '</div>'
					});
					$("#serverlist").html(html);
				}
			});
	}
	
	//节点初始表格
	function initDataTable(){
		$.jgrid.defaults.styleUI="Bootstrap";
		$("#tableData").jqGrid({
			url:"${request.contextPath}/node/getNodeDataList",
			datatype:"json",
			height: window.innerHeight-heights-150,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: false,//复选框   
			rownumbers: true,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["节点名称","节点类型"/* ,"数据","操作" */],
			colModel:[
			          {name:"id",index:"id",width:100,sortable:false},
			          {name:"nodeModel",index:"nodeModel",width:90,sortable:false,formatter:function(cellvalue, options, rowdata){
					    	if(cellvalue=='PERSISTENT'){
					    		return "持久节点";
					    	}else{
					    		return "临时节点";
					    	}
			           }}/* ,
			          {name:"data",index:"data",width:120},
				      {name:"id",index:"id",width:150,sortable:false,formatter:function(cellvalue, options, rowdata){
				    	//  var html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
			        	//	  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
			        	var html='<a href="#" class="table-link" title="查看详情" onclick="readDetails(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-search"></i>详情</span></a>';
						return html;
			          }} */
			       ],
			pager:"#tablePager",
			viewrecords:true,
			caption:"节点列表",
			hidegrid:false
			}).jqGrid('navGrid','#tablePager',{refresh:true,add:false,edit:false,del:false,search:false});
		 $(".ui-jqgrid-caption").html($("#btns").html());
		 $(window).bind("resize",function(){
			var width=$(".jqGrid_wrapper").width();
			$("#tableData").setGridWidth(width);
			$("#tableData").setGridHeight(window.innerHeight-heights-150);
		 });
		 $("#treeDiv").height(window.innerHeight-heights);
	}
	var selectNode;
	var jstree;
	function getServerTree(htmlObj){
		if($(htmlObj).find("span[name='status']").html() == "ONLINE"){
			ip = $(htmlObj).find("span[name='ip']").html();
			port = $(htmlObj).find("span[name='port']").html();
			if(jstree!=undefined){
				jstree.jstree(true).refresh(); 
				return ;
			}
			$('#treeData').data('jstree', false).empty();
			jstree = $("#treeData").jstree({
		           "core": {
		        	   "themes" : {
		                    "responsive": false
		                }, 
		               'data': {
		                   'url': '${request.contextPath}/tree/getTree',
		                   'dataType': 'json',
		                   'data': function (node) { // 传给服务端点击的节点
		                       return { path: node.id == "#" ? "" : node.id,ip:ip,port:port };
		                   },
		               },
		               "state" : { "key" : "demo3" }
		           }
		      }).bind('select_node.jstree', function(obj,e) {
				  selectNode = e.node.id;
		    	  var param={node:e.node.id,host:ip+":"+port};
		    	  $("#tableData").jqGrid('setGridParam',{ postData:param,}).trigger('reloadGrid');  
		    });
			
		}else if($(htmlObj).find("span[name='status']").html() == "OFFLINE"){
			parent.opentip("该服务不在线！");
			return;
		}
	}

	function validateData(){
		var url="${request.contextPath}/node/validate";
		$.ajax({
			type:"post",
			url : url,
			data:{},
			async:true,
			success : function(data) {
				if(data.success){
					parent.opentip("同步成功！");
				}else{
					parent.opentip("同步失败！");
				}
			},
			error: function(){
				parent.opentip("同步失败！");
			}
		});
	}
	
	function readDetails(rowId){
	    var rowData = $('#tableData').jqGrid('getRowData',rowId);
		console.log(rowData);
		$("#dataPath").val(selectNode+"/"+rowId);
		$("#dataArea").val(rowData.data);
		$("#xiangqing").show();
	}
	
	function closesDetails(){
		$("#xiangqing").hide();
	}
	
	/* function editForm(id){
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
	}  */
	
	// 递归删除节点
	/*
	function delForm(path){
		var selectData = $("#treeData").jstree(true).get_selected();
		console.log(selectData[0]);
		if(selectData[0] != "/"){
			path = selectData[0]+"/"+path;
		}else{
			path = "/"+path;
		}
		parent.confirms("确定要删除该节点及其所有子节点？",function(){
			 $.ajax({
				type:"post",
				url : '${request.contextPath}/node/delete',
				data : {"path":path},
				success : function(data) {
					if(data.success){
						parent.opentip("删除成功！");
						$("#tableData").trigger("reloadGrid");
					}else{
						parent.opentip("删除失败！");
					} 
				},
				error:function(){
					parent.opentip("删除失败！");
				}
			}); 
		});
	} */
	</script>
</body>
</html>