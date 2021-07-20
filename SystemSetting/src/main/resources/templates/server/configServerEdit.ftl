<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>配置服务管理编辑</title> 
<#include "/common/include.ftl" />
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<input type="hidden" id="id" name="id" value="${serverInfo.id}">
							<input type="hidden" id="version" name="version" value="${serverInfo.version}">
							<input type="hidden" id="status" name="status" value="${serverInfo.status}">
							
							<input type="hidden" id="cascadeDomain" name="cascadeDomain" value="${serverInfo.cascadeDomain}">
							<input type="hidden" id="orgId" name="orgId" value="${serverInfo.orgId}">
							<input type="hidden" id="orgName" name="orgName" value="${serverInfo.orgName}">
							
							<!-- <input type="hidden" id="confingFalg" name="confingFalg" value="${serverInfo.confingFalg}"> -->
							<input type="hidden" id="onlineStatus" name="onlineStatus" value="${serverInfo.onlineStatus}">
							<div class="form-group form-group-select2">
								<label>服务类型</label>
								<select class="" id="serverType" name="serverType" style="width: 100%;margin-left: 5px;">
									<option>1</option>
									<option>2</option>
									<option>3</option>
									<option>4</option>
									<option>5</option>
								</select>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">服务名</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="serverName" name="serverName" placeholder="服务名" value="${serverInfo.serverName}">
								</div>
							</div>
							<div class="form-group">
								<label>服务标识</label>
								<select class="form-control" id="confingFalg" name="confingFalg">
									<!-- <option value="1">第三方服务</option>
									<option value="2">平台服务</option> -->
								</select>
							</div>
							<div class="form-group">
								<label>地址类型</label>
								<select class="form-control" id="addressType" name="addressType">
									<option value="IP">IP</option>
									<option value="ComputerName">计算机名</option>
									<option value="DomainName">域名</option>
								</select>
							</div>
							<div class="form-group">
								<label for="inputEmail1" class="col-lg-2 control-label">服务地址</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="serverIp" name="serverIp" placeholder="IP地址" data-validate="fqdn_or_ip" value="${serverInfo.serverIp}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">服务所属域</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="domain" name="domain" placeholder="服务所属域" value="${serverInfo.domain}">
								</div>
							</div>
							<div class="form-group">
								<label for="inputPassword1" class="col-lg-2 control-label">机构编码</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="orgCode" name="orgCode" placeholder="机构编码" value="${serverInfo.orgCode}">
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
	        });
		 var optionHtml = "";
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
		 $("#serverType").html(optionHtml);
		 if("${serverInfo.serverType}"!=""){
			 $("#serverType").val("${serverInfo.serverType}"); 
			 $("#serverType").attr("disabled","disabled");
		 }
		 $("#serverType").select2();
		$("#serverType").on("change", function (evt) {
			if("${serverInfo.serverName}"==""){
				$("#serverName").val($(this).select2('data').text);
			}
		});
		 
		if("${serverInfo.serverName}"==""){
			$("#serverName").val($("#serverType").select2('data').text);
		}
		 if("${serverInfo.addressType}"!="" && "${serverInfo.addressType}"!=undefined){
			 $("#addressType").val("${serverInfo.addressType}");
		 }
		 
		 optionHtml = "";
		 $.ajax({
				type:"post",
				url : '${request.contextPath}/serverInfo/getConfigFlagEnum',
				data:{},
				async: false,
				success : function(data) {
					if(!!data.data){
						let dd = data.data;
						for(var i in dd){
							optionHtml+="<option value="+i+">"+dd[i]+"</option>";
						}
					}
				}
		}); 
		$("#confingFalg").html(optionHtml);
		// 第三方服务或平台服务选择框
		if("${serverInfo.confingFalg}"!=""){
		   $("#confingFalg").val("${serverInfo.confingFalg}"); 
		}
	});
	
	function typeChange(typeName){
	
	}
	
	//提交
	function submitSave(){
		$("#submitBt").attr("disabled", true);
	    $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        }
		$("#serverType").removeAttr("disabled");		
		 $("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/serverInfo/saveServer",
	        datatype:'json',
	        success: function(data){
				if(data){
					parent.opentip("保存成功！");
				}else{
					parent.errorMsg("保存失败！");
				}
	        	parent.layer.close(index);
	        },
			error:function(e){
				parent.errorMsg("因系统繁忙，若未修改成功请稍后再试！");
				console.log(e);
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