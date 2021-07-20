<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>配置服务管理编辑</title> <#include "/common/include.ftl" />
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<input type="hidden" id="id" name="id" value="${serverType.id}">
							<div class="form-group">
								<label for="inputEmail1" class="col-lg-2 control-label">服务类型</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="serverType" name="serverType" placeholder="服务类型" data-validate="fqdn_or_ip" value="${serverType.serverType}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">服务类型名称</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="serverTypeName" name="serverTypeName" placeholder="服务类型名称" value="${serverType.serverTypeName}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">服务说明</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="serverRemark" name="serverRemark" placeholder="服务说明" value="${serverType.serverRemark}">
								</div>
							</div>
						</form>
						<div style="text-align: center;bottom: 0px;left: 40%;position: fixed;">
							<div class="form-group">
								<div class="col-lg-offset-2 col-lg-10">
									<button type="button" class="btn btn-primary" onclick="submitSave()">保存</button>&nbsp;&nbsp;
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
		 /* $('#centerForm').bootstrapValidator({
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
	                            regexp: /^(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9])\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\.(25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9]):\d{0,5}$/,
	                            message: '请输入正确的IP地址，格式：ip:端口'
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
		 var optionHtml = "";
		 $.ajax({
				type:"post",
				url : '${request.contextPath}/serverType/getNoUseType',
				data:{serverId:'${serverInfo.id}'},
				async:false,
				success : function(data) {
					 $.each(data,function(i,item){
						 optionHtml+="<option value="+item.serverType+">"+item.serverTypeName+"</option>";
					 });
				}
			}); 
		 
		 $("#serverType").html(optionHtml);
		 
		 if("${serverInfo.serverType}"!=""){
			 $("#serverType").find("option").each(function(){
				if("${serverInfo.serverType}"==$(this).html()){
					 $("#serverType").val($(this).val());
				} 
			 }); 
		 }
		 
		 
	});
	
	//提交
	function submitSave(){
	   /*  $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        }  */
		var exist = false;
		
		$("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/serverType/exist",
	        datatype:'json',
			async:false,
	        success: function(data){
	        	 exist = data;
	        }
	    });
		
		if(exist){
			parent.opentip("服务类型已存在！");
			return;
		}
		
		$("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/serverType/saveType",
	        datatype:'json',
	        success: function(data){
	        	 parent.opentip("保存成功！");
	        	 parent.layer.close(index);
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