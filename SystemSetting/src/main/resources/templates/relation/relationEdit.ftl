<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>流向编辑</title> 
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
							<input type="hidden" id="id" name="id" value="${relation.id}">
							<input type="hidden" id="destEntranceId" name="destEntranceId" value="${relation.destEntranceId}">
							<div class="form-group">
								<label for="inputEmail1" class="col-lg-2 control-label">流向名称</label>
								<div class="col-lg-10">
									<input type="text" class="form-control" id="relationName" name="relationName" placeholder="流向名称" value="${relation.relationName}">
								</div>
							</div>
							<div class="form-group form-group-select2">
								<label>源服务类型</label>
								<select class="select2Class" id="srcServerType" name="srcServerType">
									
								</select>
							</div>
							
							<div class="form-group">
								<label>源服务</label>
								<select class="form-control" id="srcServerId" name="srcServerId">
									
								</select>
							</div>
							
							<div class="form-group">
								<label>消费功能码</label>
								<input type="text" class="form-control" id="srcConsumerFc" name="srcConsumerFc" placeholder="消费功能码" value="${relation.srcConsumerFc}">
								<!-- <select class="form-control" id="srcConsumerFc" name="srcConsumerFc">
									
								</select> -->
							</div>
							
							<div class="form-group form-group-select2">
								<label>目标服务类型</label>
								<select class="select2Class"id="destServerType" name="destServerType">
									
								</select>
							</div>
							
							<div class="form-group">
								<label>目标服务</label>
								<select class="form-control" id="destServerId" name="destServerId">
									
								</select>
							</div>
							
							<div class="form-group form-group-select2">
								<label>目标服务功能码</label>
								<select class="select2Class" id="destProviderFc" name="destProviderFc">
									
								</select>
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
	var idNameMap;
	function getServerSelectData(){
		$.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/getServerSelectData',
			data:{},
			async:false,
			success : function(data) {
				selectData = data;
				idNameMap = data.idNameMap;
				var typeNameMap = data["typeNameMap"];
				
				// 初始源服务相关下拉框数据
				iniTypeSelect(typeNameMap, "srcServerType", "${relation.srcServerType}");
				typeSelectChange("srcServerType");
				iniIdSelect($("#srcServerType").val(), "srcServerId", "${relation.srcServerId}")
				
				// 初始目标服务相关下拉框数据
				iniTypeSelect(typeNameMap, "destServerType", "${relation.destServerType}");
				typeSelectChange("destServerType");
				iniIdSelect($("#destServerType").val(), "destServerId", "${relation.destServerId}")
				
				// 设置目标功能码初始值
				getFcData($("#destServerId").val(),"${relation.destProviderFc}")
				/* if("${relation.destProviderFc}" != undefined){
					$("#destProviderFc").val("${relation.destProviderFc}");
				} */
			}
		});
	}
	
	// 初始化服务类型选择框, iniData为修改时初始值
	function iniTypeSelect(data, id, iniData){
		var optionHtml = "";
		for(var d in data){
			optionHtml+="<option value="+d+">"+data[d]+"</option>";
		}
		$("#"+id).html(optionHtml);
		if(iniData != undefined){
			$("#"+id).val(iniData);
		}
		$('#'+id).select2();
	}
	
	function typeSelectChange(id){
		$("#"+id).change(function(){
			var type = $(this).val();
			if(id == "srcServerType"){
				iniIdSelect(type, "srcServerId");
			}else{
				iniIdSelect(type, "destServerId");
			}
		});
	}
	
	// 初始化服务ID下拉框
	function iniIdSelect(type, id, iniData){
		var optionHtml = "";
		var data = selectData["typeIdsMap"][type];
		for(var d in data){
			optionHtml+="<option value="+data[d]+">"+idNameMap[data[d]]+"</option>";
		}
		$("#"+id).html(optionHtml);
		if(id == "destServerId"){
			destServerIdChange();
			getFcData($("#destServerId").val());
		}
		if(iniData != undefined){
			$("#"+id).val(iniData);
		}
	}
	
	function destServerIdChange(){
		$("#destServerId").change(function(){
			getFcData($(this).val());
		});
	}
	
	function getFcData(serverId,iniData){
		$.ajax({
			type:"post",
			url : '${request.contextPath}/entrance/getFcs',
			data:{"serverId" : serverId},
			async:false,
			success : function(data) {
				fcsData = data;
				iniFcSelect("destProviderFc",iniData);
			}
		});
	}
	
	var fcsData = null;
	function iniFcSelect(id, iniData){
		var optionHtml = "";
		for(var d in fcsData){
			optionHtml+="<option value="+d+">"+d+"</option>";
		}
		$("#"+id).html(optionHtml);
		if(iniData != undefined){
			$("#destProviderFc").val(iniData);
		}
		$("#"+id).change(function(){
			var value = $(this).val();
			var destEntranceId = fcsData[value];
			$("#destEntranceId").val(destEntranceId);
		});
		$("#destEntranceId").val(fcsData[$("#destProviderFc").val()]);
		$('#'+id).select2();
	}
	
	//提交
	function submitSave(){
		$("#destEntranceId").val(fcsData[$("#destProviderFc").val()]);
	    $('#centerForm').data('bootstrapValidator').validate();  
        if(!$('#centerForm').data('bootstrapValidator').isValid()){
       	 return ;  
        } 
		 $("#centerForm").ajaxSubmit({
	        type: 'post',  
	        url:"${request.contextPath}/relation/save",
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