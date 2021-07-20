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
							<input type="hidden" id="id" name="id" value="${agreementType.id}">
							<div class="form-group">
								<label for="inputEmail1" class="col-lg-2 control-label">协议类型</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="type" name="type" placeholder="协议类型" data-validate="fqdn_or_ip" value="${agreementType.type}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">服务类型说明</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="remarks" name="remarks" placeholder="协议类型说明" value="${agreementType.remarks}">
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
	});
	
	//提交
	function submitSave(){
	   /*  $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        }  */
		 $("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/agreement/saveAgreement",
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