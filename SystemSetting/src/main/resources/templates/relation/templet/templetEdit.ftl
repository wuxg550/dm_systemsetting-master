<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>模板编辑</title> 
<#include "/common/include.ftl" />
<style>
.select2Class{
	width:99%;
}
</style>
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<input type="hidden" id="id" name="id" value="${templet.id}">
							<div class="form-group form-group-select2">
								<label>源服务类型</label>
								<select class="select2Class" id="srcServerType" name="srcServerType">
									
								</select>
							</div>
							
							<div class="form-group">
								<label>消费功能码</label>
								<input type="text" class="form-control" id="srcConsumerFc" name="srcConsumerFc" placeholder="消费功能码" value="${templet.srcConsumerFc}">
								<!-- <select class="form-control" id="srcConsumerFc" name="srcConsumerFc">
									
								</select> -->
							</div>
							
							<div class="form-group form-group-select2">
								<label>目标服务类型</label>
								<select class="select2Class"id="destServerType" name="destServerType">
									
								</select>
							</div>
							
							<div class="form-group form-group-select2">
								<label>目标服务功能码</label>
								<input type="text" class="form-control" id="destProviderFc" name="destProviderFc" placeholder="目标功能码" value="${templet.destProviderFc}">
								<!-- <select class="select2Class" id="destProviderFc" name="destProviderFc">
									
								</select> -->
							</div>
							
							<div class="form-group form-group-select2">
								<label>描述</label>
								<input type="text" class="form-control" id="description" name="description" placeholder="对模板的描述" value="${templet.description}">
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
	
	var selectData = null;// 用于存放所有服务信息数据
	$(function(){
		getServerSelectData();
		$('#centerForm').bootstrapValidator({
            message: 'This value is not valid',
            feedbackIcons: {
                valid: 'glyphicon glyphicon-ok',
                invalid: 'glyphicon glyphicon-remove',
                validating: 'glyphicon glyphicon-refresh'
            },
            fields: {
            	
            }
	     });
	});
	
	function getServerSelectData(){
		$.ajax({
			type:"post",
			url : '${request.contextPath}/serverType/getNoUseType',
			data:{},
			async:false,
			success : function(data) {
				selectData = data;
				
				// 初始源服务相关下拉框数据
				iniTypeSelect(data, "srcServerType", "${templet.srcServerType}");
				
				// 初始目标服务相关下拉框数据
				iniTypeSelect(data, "destServerType", "${templet.destServerType}");
			}
		});
	}
	
	// 初始化服务类型选择框, iniData为修改时初始值
	function iniTypeSelect(data, id, iniData){
		var optionHtml = "";
		for(var d in data){
			optionHtml+="<option value="+data[d].serverType+">"+data[d].serverTypeName+"</option>";
		}
		$("#"+id).html(optionHtml);
		if(iniData != undefined){
			$("#"+id).val(iniData);
		}
		$('#'+id).select2();
	}
	
	//提交
	function submitSave(){
	    $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        }

		var complete = true;
		var centerForm = $("#centerForm").serializeArray();
		console.log(centerForm);
		for(var i in centerForm){
			if(centerForm[i].name == "id"){
				continue;
			}
			if(!centerForm[i].value){
				complete = false;
				break;
			}
		}
		
		if(!complete){
			parent.opentip("未填写完整！");
			return;
		}
		
		var exist = false;
		if($("#id").val() == "" || $("#id").val() == null){
			$("#centerForm").ajaxSubmit({
				type: 'post', 
				async:false,
				url:"${request.contextPath}/relation/templet/checkExist",
				datatype:'json',
				success: function(data){
					 exist = data.exist;
				}
			});
		}
		
		if(exist){
			parent.opentip("该模板已存在！");
			return;
		}
		
		$("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/relation/templet/save",
	        datatype:'json',
	        success: function(data){
	        	if(data.success){
	        		parent.opentip("保存成功！");
		        	colses();
				}else{
					parent.opentip("保存失败！");
				} 
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