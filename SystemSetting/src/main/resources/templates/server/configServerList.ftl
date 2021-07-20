<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务管理列表</title>
<#include "/common/include.ftl" /> 
</head>
<style type="text/css">
#xiangqing{
	display: none;
	text-align:center;
	position:absolute;
	top:0px;
	left:0px;
	width:60%;
	heith:80%;
	margin-top:18%;
	margin-left:10%;
	z-index:9;
	padding: 0px 10px 25px 0px;
}

#xiangqing label{
	font-size:16px;
}

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
<body>

	<div style="display: black;padding:10px 0px 0px 10px; margin-top:5px;" id="searchDiv">
		<div class="clearfix">
		<form class="form-inline" role="form" id="serverForm">
			<div class="form-group form-group-select2">
				<font>服务类型:</font>
				<select class="" id="serverType" name="serverType"
					style="width: 180px;margin-left: 5px;">
				</select>
			</div>
			<div class="form-group">
				<font>服务名称:</font>
				<input class="form-control" id="serverName" name="serverName"  type="text" style="width:150px;" placeholder="服务名称"  />
			</div>
			<div class="form-group">
				<font>服务地址:</font>
				<input class="form-control" id="serverIp" name="serverIp"  type="text" style="width:150px;" placeholder="服务地址"  />
			</div>
			<div class="form-group">
				<font>服务ID:</font>
				<input class="form-control" id="id" name="id"  type="text" style="width:150px;" placeholder="服务主键"  />
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
		<button type="button" class="btn btn-info" onclick="autoConfigRelation(0)"><div title="提示：1.根据流向模板配置未有的流向，已配置的流向不会被覆盖或删除；2.只配置在线的源服务">一键配置流向</div></button>
		<button type="button" class="btn btn-warning" onclick="autoConfigRelation(1)"><div title="提示：1.根据流向模板配置流向，已配置的流向会被覆盖或删除；">强制配置流向</div></button>
		<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="validateData()" title="从zookeeper服务同步数据至系统配置服务">
			<i class=""></i> 同步数据
		</button>
		<button class="btn btn-danger" type="button" id="add-btn" onclick="batchDelete()" title="批量删除已勾选的离线服务">
			<i class=""></i> 删除离线服务
		</button>
		<!-- <button type="button" class="btn btn-warning" onclick="registeThirdPartyServer()"><div title="提示：当数据库重装或数据重新导入时，可点击此按钮。按钮功能：1.将数据库中第三方服务数据同步至zookeeper节点；2.将zookeeper上非第三方服务的服务信息同步至数据库；">注册第三方服务</div></button> -->
		<font color="red" id="serverIdTip"></font>
	</div>
	
	<!-- 详情页面 -->
	<div id="xiangqing" style="background-color: #dff0d8;border: 1px solid black;">
		<div style="text-align: right;top: 0px;left: 80%;">
			<div class="form-group">
				<div class="col-lg-offset-2 col-lg-10" style="padding-bottom: 10px;">
					<button type="button" class="" onclick="closesDetails()">X</button>
				</div>
			</div>
		</div>
		<div class="layui-layer-content">
			<div class="form-group">
				<label for="dataPath" class="col-lg-10 control-label" id="serverInfoLabel"></label>
			</div>
			<div class="form-group" id="serverGroup">
				
			</div>
		</div>
	</div>
	
	<div id="validating">
		<font size="14">正在操作...</font>
		<img height="24" alt="正在操作..." src="${request.contextPath}/img/select2-spinner.gif">
	</div>
	
	<script type="text/javascript">
	$(document).ready(function(){
		$.jgrid.defaults.styleUI="Bootstrap";
		
		serverTypeSelect();
		
		$("#tableData").jqGrid({
			url:"${request.contextPath}/serverInfo/getServerPage",
			datatype:"json",
			height: window.innerHeight-heights-55,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: true,//复选框   
			rownumbers: true,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["服务类型","服务名称","服务IP","状态","服务所属域","级联域","服务所属机构名称","操作"],
			colModel:[
			          {name:"serverType",index:"serverType",width:80,sortable:false},
			          {name:"serverName",index:"serverName",width:70,sortable:false},
			          {name:"serverIp",index:"serverIp",width:80},
			          {name:"onlineStatus",index:"onlineStatus",width:40,formatter:function(cellvalue, options, rowdata){
			        	  var html = "";
						  if(rowdata.confingFalg == "1"){
							return '<span class="label label-warning">---</span>'; 
						  }
			        	  if(cellvalue == "0"){
								return '<span class="label label-success">在线</span>';
						  }else{
								return '<span class="label label-danger">离线</span>'; 
						  }
			          }},
			          {name:"domain",index:"domain",width:30,},
					  {name:"cascadeDomain",index:"cascadeDomain",width:30,},
					  {name:"orgName",index:"orgName",width:70,},
				      {name:"id",index:"id",width:150,sortable:false,formatter:function(cellvalue, options, rowdata){
				    	  var html='<a href="#" class="table-link" title="编辑" onclick="editForm(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
				    	  if(rowdata.confingFalg != "1"){
				    		  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
				    	  }
				    	  
		        		  html += '<a href="#" class="table-link" title="服务详情" onclick="getServerDetail(\''+cellvalue+'\''+',\''+rowdata.serverName+'\')"><span class="label label-primary"><i class="fa fa-search"></i>详情</span></a>';
						  html += '<a href="#" class="table-link" title="运行配置" onclick="getRunningConfig(\''+cellvalue+'\''+',\''+rowdata.serverName+'\')"><span class="label label-primary"><i class="fa fa-search"></i>运行配置</span></a>';
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
			$("#tableData").setGridHeight(window.innerHeight-heights-55);
		 });
	});
	
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
		
		$("#serverType").html(optionHtml);
		$("#serverType").select2();
	}
	
	var ServerRoleType = ["Unknown","Master","Slave"];
	var labelTemp = '<label for="dataPath" class="col-lg-10 control-label">#serverGroupInfo#</label>';
	function getServerDetail(id, serverName){
	//	$("#serverInfoLabel").html(serverName + "ID："+ id);
	//	$("#serverGroup").empty();
		$.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/getServerDetail',
			data:{serverId:id},
			async: false,
			success : function(data) {
				/*if(data.votee.length > 0){
					$("#serverGroup").append(labelTemp.replace("#serverGroupInfo#", "集群信息"));
					var groupData = data.votee;
					for(var i in groupData){
						if(i == 0){
							$("#serverGroup").append(labelTemp.replace("#serverGroupInfo#", groupData[i]+" <font color='green'>[Master]</font>"));
						}else{
							$("#serverGroup").append(labelTemp.replace("#serverGroupInfo#", groupData[i]+" <font color='red'>[Slave]</font>"));
						}
						
					}
				}*/
				var html = serverName + "ID："+ id;
				if(!!data.role){
					html += " | 集群角色：" + ServerRoleType[data.role];
				}
				if(!!data.serverInfo && !!data.serverInfo.version){
					html += " | 版本号：" + data.serverInfo.version;
				}
				$("#serverInfoLabel").html(html);
			}
		});
		
		$("#xiangqing").show();
	}
	
	function closesDetails(){
		$("#xiangqing").hide();
	}
	
	function editForm(id){
		var title="新增服务";
		var url="${request.contextPath}/serverInfo/configServerEdit";
		if(id!=undefined){
			title="编辑服务";
			url="${request.contextPath}/serverInfo/updataServer?id="+id;
		}
		parent.openwin(title,url, 600, 500, function(){
			 $("#tableData").trigger("reloadGrid");
		});
	}
	
	var delId = "";
	function delForm(id){
		parent.confirms("您确定要删除该服务？",function(){
			delForm1(id);
		});
	}
	
	function delForm1(id){
		$.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/delServer',
			data:{id:id},
			async: false,
			success : function(data) {
				if(data.success){
					parent.opentip("删除成功！");
				}else{
					parent.opentip(data.msg);
				}
				$("#tableData").trigger("reloadGrid");
			},
			error : function(e){
				if(delId != id){
					delId = id;
					sleep(500);
					console.log("删除发生错误！再次尝试删除");
					delForm1(id);
				}else{
					parent.errorMsg("系统繁忙！若未删除成功请稍后再试！");
					$("#tableData").trigger("reloadGrid");
				}
			}
		});
	}
	
	function sleep(numberMillis) {
		var now = new Date();
		var exitTime = now.getTime() + numberMillis;
		while (now.getTime() < exitTime) {
			now = new Date();
		}
	}
	
	function updateStatus(id,status,cumlum){
		var title = "";
		if(status == "0" && cumlum != "confingFalg"){
			title = "启用";
		}else if(status == "1" && cumlum != "confingFalg"){
			title = "禁用";
		}else{
			title = "注册";
		}
		parent.confirms("您确定要"+title+"该服务？",function(){
			 $.ajax({
				type:"post",
				url : '${request.contextPath}/serverInfo/updServer',
				data:{id:id,status:status,cumlum:cumlum},
				success : function(data) {
					if(data == 1){
						parent.opentip(""+title+"成功！");
					}else{
						parent.opentip(""+title+"失败！");
					}
					$("#tableData").trigger("reloadGrid");
				}
			}); 
		});
	}
	
	function autoConfigRelation(type){
		var url = "";
		var title = "";
		if(type==1){
			url = '${request.contextPath}/relation/imposedConfig';
			title = '确定使用强制配置流向?';
		}else{
			url = '${request.contextPath}/relation/autoConfig';
			title = '确定使用一键配置流向?';
		}
		var serverIds = $("#tableData").jqGrid('getGridParam','selarrrow');
		if(serverIds.length < 1){
			parent.opentip("请勾选需要配置的服务！");
			return;
		}
		parent.confirms(title,function(){
			var serverTypes = new Array();
			for(var i in serverIds){
				var rowData = $("#tableData").jqGrid('getRowData',serverIds[i]);
				serverTypes.push(rowData.serverType);
			}
			$.ajax({
				type:"post",
				url : url,
				data:{"serverTypes":JSON.stringify(serverTypes), "serverIds":JSON.stringify(serverIds)},
				success : function(data) {
					if(data.success){
						parent.opentip("配置成功！");
					}else{
						parent.opentip("配置失败！");
					}
				}
			});
		});
	}
	
	function validateData(){
		var url="${request.contextPath}/node/validate";
		$("#validating").show();
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
				$("#tableData").trigger("reloadGrid");
				$("#validating").hide();
			},
			error: function(){
				parent.opentip("同步失败！");
				$("#validating").hide();
			}
		});
	}
	
	function search(){
		var url = "${request.contextPath}/serverInfo/getServerPage?"+$('#serverForm').serialize();
		$("#tableData").jqGrid('setGridParam',{url: url,postData : {},page:1}).trigger("reloadGrid");
	}
	
	function getRunningConfig(id,serverName){
		var url="${request.contextPath}/serverInfo/runningConfig?id="+id+"&serverName="+serverName;
		parent.openwin("编辑运行配置",url, 600, 500, function(){
			 
		});
	}
	
	function batchDelete(){
		var serverIds = $("#tableData").jqGrid('getGridParam','selarrrow');
		if(serverIds.length < 1){
			parent.opentip("请勾选需要删除的离线服务！");
			return;
		}
		parent.confirms("您确定要删除已勾选的服务？",function(){
			 $.ajax({
				type:'POST',
				url : '${request.contextPath}/serverInfo/batchDelete',
				data:{"serverIds":JSON.stringify(serverIds), _method: 'DELETE'},
				async:false,
				success : function(data) {
					if(data.success){
						parent.opentip("成功删除"+data.data+"条记录");
						$("#tableData").trigger("reloadGrid");
					}else{
						if(!!data.msg){
							parent.errorMsg(data.msg);
						}
					}
				},
				error:function(e){
					parent.errorMsg("发生异常！请检查zookeeper服务是否能正常连接");
				}
			}); 
		});
	}
	
	function registeThirdPartyServer(){
		$("#validating").show();
		var url="${request.contextPath}/serverInfo/registeThirdPartyServer";
		$.ajax({
			type:"post",
			url : url,
			data:{},
			async:true,
			success : function(data) {
				parent.opentip("注册完成！");
				$("#tableData").trigger("reloadGrid");
				$("#validating").hide();
			},
			error: function(){
				parent.opentip("注册失败！");
				$("#validating").hide();
			}
		});
	}
	</script>
</body>
</html>