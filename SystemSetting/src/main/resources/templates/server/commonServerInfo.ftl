<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<style type="text/css">
.spanStyle{
    margin-top: 4px;
	cursor: pointer;
}
.change{
	margin-top: 10px;
}
.leftClass{
	float:left;
}

.spanButton{
	padding :0px 10px 10px 10px;
}

.spanButton:hover{
	background-color : gray;
}

#configGroup>div{
	padding : 10px;
}
</style>
<title>运行配置</title> 
<link rel="stylesheet" href="${request.contextPath}/css/colorpicker/bootstrap-colorpicker.min.css">
<#include "/common/include.ftl" />
<script type="text/javascript" src="${request.contextPath}/js/colorpicker/bootstrap-colorpicker.min.js"></script>
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<div id="configGroup" name="configGroup" class="form-group" style="padding:5px 0px 5px 0px;">
								<div class="col-lg-10">
									<div style="width:10%" class="leftClass spanStyle">级联域:</div>
									<div style="width:90%" class="leftClass">
										<!-- <input type="text" class="form-control" name="cascadeDomain" placeholder="" value="${infos.cascadeDomain}" style="width: 50%;"> -->
										<select class="form-control" name="cascadeDomain" style="width: 50%;">
											<option selected="selected" value="IOTMP">IOTMP</option>
										</select>
									</div>
								</div>
								<div class="col-lg-10">
									<div style="width:10%" class="leftClass spanStyle">机构ID:</div>
									<div style="width:90%" class="leftClass">
										<input type="text" class="form-control"  name="orgId" placeholder="" value="${infos.orgId}" style="width: 50%;">
									</div>
								</div>
								<div class="col-lg-10">
									<div style="width:10%" class="leftClass spanStyle">机构名称:</div>
									<div style="width:90%" class="leftClass">
										<input type="text" class="form-control" name="orgName" placeholder="" value="${infos.orgName}" style="width: 50%;">
									</div>
								</div>
							</div>
						</form>
						<div style="text-align: center;left: 20%;position: fixed;">
							<div class="form-group">
								<button type="button" class="btn btn-primary" onclick="submitSave()">保存</button>&nbsp;&nbsp;
								<#-- <button type="button" class="btn btn-default" onclick="colses()">取消</button> -->
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	$(function(){
        /*$.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/getCommonServerInfo',
			async: false,
			data : {},
			success : function(data) {
				console.log(data);
				$("#centerForm").form("load", data);
			}
		}); */ 
		var cascadeDomain = "${infos.cascadeDomain}";
		if(!!cascadeDomain){
			$("input[name='cascadeDomain']").val(cascadeDomain);
		}
	});
	
	//提交
	function submitSave(){
		$.ajax({
	        type: 'post',  
	        url:"${request.contextPath}/serverInfo/saveCommonServerInfo",
	        datatype:'json',
			data : $("#centerForm").serialize(),
	        success: function(data){
				if(data.success){
					parent.opentip("保存成功！");
				}else{
					parent.errorMsg(data.msg);
				}
	        },
			error: function(e){
				alert("发生错误！");
			}
	    }); 
	}
	</script>
</body>
</html>