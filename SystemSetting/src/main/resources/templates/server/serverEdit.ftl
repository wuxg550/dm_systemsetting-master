<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务管理编辑</title> <#include "/common/include.ftl" />
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<input type="hidden" id="id" name="id" value="${zkserver.id}">
							<div class="form-group">
								<label for="inputEmail1" class="col-lg-2 control-label">主机地址</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="ip" name="ip" placeholder="主机ip地址" data-validate="fqdn_or_ip" value="${zkserver.ip}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">端口</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="port" name="port" placeholder="端口" value="${zkserver.port}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">配置文件</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="path" name="path" placeholder="配置文件" value="${zkserver.path}">
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
		 $('#centerForm').bootstrapValidator({
	            message: 'This value is not valid',
	            feedbackIcons: {
	                valid: 'glyphicon glyphicon-ok',
	                invalid: 'glyphicon glyphicon-remove',
	                validating: 'glyphicon glyphicon-refresh'
	            },
	            fields: {
	                ip: {
	                    validators: {
	                        notEmpty: {
	                            message: '主机地址不能为空'
	                        },
	                        regexp: {
	                            regexp: /^(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])\.(\d{1,2}|1\d\d|2[0-4]\d|25[0-5])$/,
	                            message: '请输入正确的主机地址'
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
	        });
	});
	
	//提交
	function submitSave(){
	    $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        } 
		 $("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/saveServer",
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