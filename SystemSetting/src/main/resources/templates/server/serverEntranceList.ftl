<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />

<title>节点树管理</title><#include "/common/include.ftl" /> 
<script src="${request.contextPath}/js/jquery.slimscroll.min.js"></script>
<style type="text/css">
.active{
  background-color: #00ff80;
  border-color: #00ff80;
}
</style>
</head>
<body>

	
<div class="main-box clearfix" style="margin-bottom: 0px;">
<div id="search-form" style="margin: 15px;">
	  
		<div class="input-group" style="display: block;">
		  <div style="float:left;font-size: 20px;">服务</div>
			<select class="" id="serverId" name="serverId"
				style="width: 180px;margin-left: 15px;">
				<option>1</option>
				<option>2</option>
				<option>3</option>
				<option>4</option>
				<option>5</option>
			</select>
			<button class="btn btn-lg btn-primary" style="margin-left: 10px; height: 34px;" onclick="search()">
					<i class="fa fa-search"></i> 搜索
			</button>
			
		</div>
	</div>
	   <div class="jqGrid_wrapper">
           <table id="tableData"></table>
           <div id="tablePager"></div>
       </div>
	</div>
	<div style="display: none;" id="btns">
		<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="addEntrance('')">
			<i class="fa  fa-plus"></i> 新增 
		</button>
	</div>
		
		
		
      
	<script type="text/javascript">
	var serverId = "";
	var id = "";
	$(function(){
		
		var serverType = "";
		/*  $.ajax({
				type:"post",
				url : '${request.contextPath}/serverInfo/getServer',
				async: false,
				success : function(data) {
					 var html = "";
					$.each(data,function(i,item){
						html += '<a href="#" class="list-group-item active" object="'+item.id+'" serverType="'+item.serverType+'">'+item.serverName+'</a>';
					});
					$(".list-group").html(html); 
				}
			}); */ 
			showTable();
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
		 
		 $("#serverId").html(optionHtml);
		 $("#serverId").select2();
	});
	
	function search(){
		 var serverId = $("#serverId").val(); 
         $("#tableData").jqGrid('setGridParam',{ 
             url:"${request.contextPath}/entrance/getEntranceList", 
             postData:{'serverId':serverId} //发送数据 
			 ,page:1
         }).trigger("reloadGrid"); //重新载入 
	}
	
    function showTable(){
    	//var postData = {"serverId":id,"serverType":serverType};
    	 $.jgrid.defaults.styleUI="Bootstrap";
			$("#tableData").jqGrid({
				url:"${request.contextPath}/entrance/getEntranceList",
				datatype:"json",
				height: window.innerHeight-heights-85,
				autowidth:true,
				altRows:true,
				shrinkToFit:true,
				multiselect: false,//复选框   
				rownumbers: true,
				rowNum:rowNums,
				rowList:rowLists,
				colNames:["服务名称","服务类型","服务IP","服务端口","协议类型","地址","操作","服务id"],
				colModel:[
				          {name:"serverName",index:"serverName",width:70,sortable:false},
				          {name:"serverType",index:"serverType",width:70,sortable:false},
				          {name:"serverIp",index:"serverIp",width:70},
				          {name:"port",index:"port",width:60},
				          {name:"protocol",index:"protocol",width:80},
				          {name:"url",index:"url",width:90},
					      {name:"id",index:"id",width:100,sortable:false,formatter:function(cellvalue, options, rowdata){
					    	  var html='<a href="#" class="table-link" title="编辑" onclick="addEntrance(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
				        		  html+='<a href="#" class="table-link" title="删除" onclick="delEntrance(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
				        		  html +='<a href="#" class="table-link" title="详情" onclick="addEntrance(\''+cellvalue+'\',\'info\')"><span class="label label-primary"><i class="fa fa-edit"></i> 详情</span></a>';
				        		  return html;
				          }},{name:"serverId",index:"serverId",hidden:true}
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
				$("#tableData").setGridHeight(window.innerHeight-heights-85);
			 });
    }
    
    function addEntrance(entranceId,info){
    	var title="新增服务入口";
    	if(entranceId == ""){
    		title="新增服务入口";
    	}else{
    		title="修改服务入口"
    	}
		var url="${request.contextPath}/entrance/serverEntranceEdit?id="+entranceId+"&info="+info;
		
		parent.openwin(title,url, 600, 400, function(){
			 $("#tableData").trigger("reloadGrid");
			 
		});
    }

    function delEntrance(entranceId,FC){
    	parent.confirms("您确定要删除该服务入口？",function(){
			 $.ajax({
				type:"post",
				url : '${request.contextPath}/entrance/delEntrance',
				data:{entranceId:entranceId},
				success : function(data) {
					 parent.opentip("删除成功！");
					 $("#tableData").trigger("reloadGrid");
				}
			}); 
		});
    }
    
	</script>
</body>
</html>