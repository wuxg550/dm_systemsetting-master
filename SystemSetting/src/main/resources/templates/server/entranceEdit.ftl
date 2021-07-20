<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<style type="text/css">
.spanStyle{
    margin-top: 6px;
	cursor: pointer;
}
.change{
	margin-top: 10px;
}
.leftClass{
	float:left;
}
</style>
<title>配置服务管理编辑</title> <#include "/common/include.ftl" />
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<input type="hidden" id="id" name="id" value="${serverEntranceDto.id}">
							<input type="hidden" id="serverId" name="serverId" value="${serverEntranceDto.serverId}">
							<input type="hidden" id="fcs" name="fcs" value="${serverEntranceDto.FCcode}">
							<div class="form-group">
								<label for="inputEmail1" class="col-lg-2 control-label">服务名称</label>
								<!-- <div class="col-lg-10">
									<input type="text" class="form-control" id="serverName" name="serverName" placeholder="服务名称" data-validate="fqdn_or_ip" value="${serverInfo.serverIp}" readOnly="readOnly">
								</div> -->
								<select class="form-control" id="serverName" name="serverName" >
								</select>
								
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">服务IP</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="serverIp" name="serverIp" placeholder="服务IP" value="${serverEntranceDto.serverIp}" readOnly="readOnly">
								</div>
							</div>
							<div class="form-group" style="display: none;">
								<label>服务类型</label>
								<input type="text" class="form-control" id="serverType" name="serverType" value="${serverEntranceDto.serverType}" readOnly="readOnly">
								<!-- <select class="form-control" id="serverType" name="serverType" >
									<option>1</option>
									<option>2</option>
									<option>3</option>
									<option>4</option>
									<option>5</option>
								</select> -->
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">协议</label>
								<select class="form-control" id="protocol" name="protocol">
									<option>1</option>
									<option>2</option>
									<option>3</option>
									<option>4</option>
									<option>5</option>
								</select>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">端口</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="port" name="port" placeholder="端口" value="${serverEntranceDto.port}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">地址</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="URL" name="URL" placeholder="地址" value="${serverEntranceDto.url}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">用户名</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="userName" name="userName" placeholder="用户名" value="${serverEntranceDto.userName}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">密码</label>
								<div class="col-lg-10">
									<input type="password" class="form-control" id="password" name="password" placeholder="密码" value="${serverEntranceDto.password}">
								</div>
							</div>
							<label for="inputPassword1" class="col-lg-2 control-label">功能码</label>
							<div id="codeList">
								<div class="form-group">
									<div class="col-lg-10">
										<div class="leftClass" style="width:90%"><input type="text" class="form-control"  name="code" placeholder="功能码" value="" style="width: 95%;"></div>
										 
										<div name="remove" class="leftClass" onclick="removeCode(this)"><span class="glyphicon glyphicon-minus" style="top: 5px;cursor: pointer;"></span>	</div>
									</div>
								</div>
							</div>
							<div style="text-align: center;bottom: 0px;left: 40%;margin-bottom:30px;">
								<div class="form-group">
									<div class="col-lg-offset-2 col-lg-10">
										<button title="添加功能码" type="button" class="btn btn-default" onclick="addCode()" style="border:0px;background-color:gray;">
											<span class="glyphicon glyphicon-plus spanStyle"></span>
										</button>
									</div>
								</div>
							</div>
						</form>
						<div style="text-align: center;bottom: 0px;left: 40%;position: fixed;">
							<div class="form-group">
								<div class="col-lg-offset-2 col-lg-10">
									<button id="submitBt" type="button" class="btn btn-primary" onclick="submitSave()">保存</button>&nbsp;&nbsp;
									<button type="button" class="btn btn-default" onclick="colses()">取消</button>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	var index = parent.layer.getFrameIndex(window.name);
	
	$(function(){
		var parentId = "${serverId}";
		var serverType = "";
		var serverHtml = "<option>请选择</option>";
		/*  $('#centerForm').bootstrapValidator({
	            message: 'This value is not valid',
	            feedbackIcons: {
	                valid: 'glyphicon glyphicon-ok',
	                invalid: 'glyphicon glyphicon-remove',
	                validating: 'glyphicon glyphicon-refresh'
	            },
	            fields: {
	            	serverIp: {
	                    validators: {
	                        notEmpty: {
	                            message: 'IP地址不能为空'
	                        },
	                        regexp: {
	                            regexp: /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])/,
	                            message: '请输入正确的IP地址，格式：ip'
	                        }
	                    }
	                },
	                port: {
	                    validators: {
	                        notEmpty: {
	                            message: '端口不能为空'
	                        },
	                        regexp: {
	                            regexp: /^([0-9]|[1-9]\d{1,3}|[1-5]\d{4}|6[0-5]{2}[0-3][0-5])$/,
	                            message: '请输入正确的端口'
	                        }
	                    }
	                }
	            }
	        }); */
	        
        $.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/getServer',
			async: false,
			success : function(data) {
				 $.each(data,function(i,item){
					 serverHtml+="<option value="+item.id+">"+item.serverName+"</option>";
				 });
			}
		}); 
	     $("#serverName").html(serverHtml);   
	     
	    if("${serverEntranceDto.serverId}"){
	    	$("#serverName").val("${serverEntranceDto.serverId}");
	    }
	    
		 /* var optionHtml = "";
		 $.ajax({
				type:"post",
				url : '${request.contextPath}/serverType/getNoUseType',
				data:{serverId:'${serverInfo.id}'},
				async: false,
				success : function(data) {
					 $.each(data,function(i,item){
						 optionHtml+="<option value="+item.serverType+">"+item.serverTypeName+"</option>";
					 });
				}
			}); 
		 
		 $("#serverType").html(optionHtml); */
		 
		 if("${serverEntranceDto.serverType}"){
		    $("#serverType").val("${serverEntranceDto.serverType}");
		  }
		
		   $("select#serverName").change(function(){
		    	getInfo($(this).val())
		    }); 
		 var agreementHtml = "";
		 $.ajax({
				type:"post",
				url : '${request.contextPath}/agreement/getAllType',
				async: false,
				success : function(data) {
					 $.each(data,function(i,item){
						 agreementHtml+="<option value="+item.type+">"+item.type+"</option>";
					 });
				}
			});
		 $("#protocol").html(agreementHtml);
		 
		 if("${serverEntranceDto.protocol}"!=""){
			 $("#protocol").val("${serverEntranceDto.protocol}");
		  }
		 
		 var fcsArray = ${serverEntranceDto.FCcode};
		 if(fcsArray!=""){
			 
			 $("#codeList").html('');
			  if(fcsArray.length>0){
				 for(var j = 0;j<fcsArray.length;j++ ){
					 addCode(j);
				 }
			  }
			 for(var i = 0;i<fcsArray.length;i++){
				 $("input[name='code']").each(function(j){
					 if($(this).val() == ""){
						 $(this).val(fcsArray[j]); 
						 /* if(parentId.split(":")[1] != fcsArray[j]){
							 $(this).parent().parent().parent().hide();
						 } */
					 }
					
				 });
			 }
		 }
		 
		 
		 
		 
		 /*  if(parentId.split(":")[1] != 'undefined'){
			 var fcsArray = [];
			  $.ajax({
	   				type:"post",
	   				url : '${request.contextPath}/entrance/getEntrance',
	   				data:{entranceId:parentId.split(":")[0]},
	   				async: false,
	   				success : function(data) {
	   				
	   				 for(var key in data){
	   					$("#"+key).val(data[key]); 
	   					$("#URL").val(data.url);
	   					$("#serverName").val(data.serverId);
	   					getInfo(data.serverId);
	   					fcsArray = eval("("+data.fcs+")");
	   				 }
	   				}
	   			}); 
			  $("#codeList").html('');
			  if(fcsArray.length>0){
				 for(var j = 0;j<fcsArray.length;j++ ){
					 addCode('code');
				 }
			  }
			 for(var i = 0;i<fcsArray.length;i++){
				 $("input[name='code']").each(function(j){
					 if($(this).val() == ""){
						 $(this).val(fcsArray[j]); 
						 if(parentId.split(":")[1] != fcsArray[j]){
							 $(this).parent().parent().parent().hide();
						 }
					 }
					
				 });
			 }
			
			  
		 } */
		if("${info}" != "undefined"){
			$("select").attr("disabled",true);
			$("input").attr("readOnly",true);
			$(".btn-primary").hide();
		} 
		 
		 
		 
	});
	
	function addCode(j){
		var style = "";
		
		var addHtml = "";
		if(j == 0){
			//addHtml = '<div class="leftClass" name="add" onclick="addCode(\'add\')"><span class="glyphicon glyphicon-plus spanStyle"></span></div>';
		}
		codeHtml = '<div class="form-group">'+
					'<div class="col-lg-10"><div class="leftClass" style="width:90%"><input type="text" class="form-control"  name="code" placeholder="功能码" value="" style="width: 95%;"></div>'+
					''+addHtml+'<div name="remove" class="leftClass" onclick="removeCode(this)"><span class="glyphicon glyphicon-minus" style="top: 5px;cursor: pointer;"></span>	</div>'+	
					'</div>'+
					'</div>';
		$("#codeList").append(codeHtml);
		if(j == 0){
		//	$("div[name='remove']").hide();
		} 
	}
	
	
	function removeCode(obj){
		$(obj).parent().parent().remove();
	}
	
	//提交
	function submitSave(){
	    /* $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        }  */
        $("#submitBt").attr("disabled", true);
        var codeArray = [];
        $("#centerForm").find("input[name='code']").each(function(){
        	codeArray.push($(this).val());
        });
        $("#fcs").val(JSON.stringify(codeArray));
		  $("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/entrance/saveEntrance",
	        datatype:'json',
	        success: function(data){
	        	 if(data){
	        		 parent.opentip("保存成功！");
	        	 }else{
	        		 parent.errorMsg("保存失败！");
	        	 }
	        	 // 此处关闭弹窗做一个延时处理
	        	 // 由于后台监测到zookeeper入口节点有变化而更新入口信息导致的表格数据id不一致
	        	 setTimeout(function(){
	        		 parent.layer.close(index);
	        	 },500);
	        }
	    }); 
	}
	
	function getInfo(id){
		  $.ajax({
 				type:"post",
 				url : '${request.contextPath}/serverInfo/getOneServer',
 				data:{id:id},
 				success : function(data) {
 					$("#serverIp").val(data.serverIp);
 					$("#serverId").val(data.id);
 					$("#serverType").val(data.serverType);
 					 /* if(data.serverType!=""){
 						 $("#serverType").find("option").each(function(){
 							if(data.serverType==$(this).val()){
 								 $("#serverType").val($(this).val());
 							} 
 						 }); 
 					 } */
 				}
 			}); 
	}
	
	
	
	//关闭
	function colses(){
		parent.layer.close(index);
	}
	</script>
</body>
</html>