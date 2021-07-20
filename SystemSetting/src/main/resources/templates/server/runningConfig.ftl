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
</style>
<title>运行配置</title> 
<link rel="stylesheet" href="${request.contextPath}/css/colorpicker/bootstrap-colorpicker.min.css">
<#include "/common/include.ftl" />
<script type="text/javascript" src="${request.contextPath}/js/colorpicker/bootstrap-colorpicker.min.js"></script>
</head>
<body>
	<div style="padding:10px;font-size:16px;">
		服务：<input type="text" class=""  name="serverName" value="${serverName}" style="width: 80%;border:0px" readonly="readonly">
	</div>

	<div class="main-box clearfix" style="margin-bottom: 0px; padding: 10px">
		<div class="row">
			<div class="col-lg-12">
				<div class="main-box">
					<div class="main-box-body clearfix">
						<form class="form-horizontal" id="centerForm">
							<input type="hidden" id="serverId" name="serverId" value="${serverId}">
							<div id="configList">
								
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
	
	<#-- 配置div模板 -->
	<div id="config_temp" style="display:none;">
		<div name="configGroup" itype="config_temp" class="form-group" style="border:solid 1px black;padding:5px 0px 5px 0px;">
			<div class="col-lg-10">
				<div style="width:10%" class="leftClass spanStyle">配置键:</div>
				<div style="width:90%" class="leftClass">
					<input type="text" class="form-control" name="cfgkey" placeholder="运行配置键" value="" style="width: 90%;">
				</div>
			</div>
			<div class="col-lg-10">
				<div style="width:10%" class="leftClass spanStyle">配置值:</div>
				<div style="width:60%" class="leftClass">
					<input type="text" class="form-control"  name="cfgval" placeholder="运行配置值" value="" style="width: 90%;">
					<input type="hidden" class="form-control"  name="valType">
				</div>
				<div style="padding:0px 10px 10px 10px;margin-top:4px;" class="leftClass"><input class="form-check-input" type="checkbox" name="isColorpicker">调色板</div>
				<div style="padding:0px 10px 10px 10px;margin-top:4px;" class="leftClass"><input class="form-check-input" type="checkbox" name="isPassword">密码</div>
			</div>
			<div class="col-lg-10">
				<div style="width:10%" class="leftClass spanStyle">名称:</div>
				<div style="width:70%" class="leftClass">
					<input type="text" class="form-control" name="cfgname" placeholder="运行配置名称" value="" style="width: 90%;">
				</div>
				<div class="leftClass spanStyle spanButton" onclick="addOne()"><span class="glyphicon glyphicon-plus spanStyle"></span></div>
				<div class="leftClass spanStyle spanButton" onclick="removeThis(this)"><span class="glyphicon glyphicon-minus" style="top:5px;cursor: pointer;"></span></div>
			</div>
		</div>
	</div>
	
	<script type="text/javascript">
	var index = parent.layer.getFrameIndex(window.name);
	$(function(){
		$("#serverName").val("${serverName}");
        $.ajax({
			type:"post",
			url : '${request.contextPath}/serverInfo/getRunningConfig',
			async: false,
			data : {id : $("#serverId").val()},
			success : function(data) {
				var mapData = JSON.parse(data);
				var configData = mapData.configData;
				if(configData.length > 0){
					initRunningConfig(configData);
				}else{
					addOne();
				}
				// cfgkeyListener();
				colorpickerCheckboxEvent();
				passwordCheckboxEvent();
				cfgvalListener();
			}
		});
	});
	
	function colorpickerCheckboxEvent(){
		// 调色板勾选框事件监听
        $("input[name='isColorpicker']").change(function(){
        	var ischecked = $(this).prop("checked");
        	var parentId = $(this).parent().parent().parent().attr("id");
        	if(ischecked){
        		$('#'+parentId + " input[name='cfgval']").colorpicker();
        		// 先禁用掉勾选框
        		$('#'+parentId + " input[type='checkbox']").attr("disabled",true);
        	}else{
        		// 未找到去除colorpicker的方法，考虑重新刷新input输入框控件
        	}
        });
	}
	
	function passwordCheckboxEvent(){
		// 调色板勾选框事件监听
        $("input[name='isPassword']").change(function(){
        	var ischecked = $(this).prop("checked");
        	var parentId = $(this).parent().parent().parent().attr("id");
        	if(ischecked){
        		$('#'+parentId + " input[name='cfgval']").attr('type','password');
        		$('#'+parentId + " input[name='valType']").val("1");
        		// 先禁用掉勾选框
        		$('#'+parentId + " input[type='checkbox']").attr("disabled",true);
        	}else{
        		$('#'+parentId + " input[name='valType']").val("0");
        	}
        });
	}
	
	function cfgkeyListener(){
		$("input[name='cfgkey']").change(function(){
			if($(this).val() == 'FontColor'){
				var parentId = $(this).parent().parent().parent().attr("id");
				$('#'+parentId + " input[name='cfgval']").colorpicker();
			}
		});
		
		$("input[name='cfgkey']").each(function(index, dom){
			if($(this).val() == 'FontColor'){
				var parentId = $(this).parent().parent().parent().attr("id");
				$('#'+parentId + " input[name='cfgval']").colorpicker();
			}
		});
	}
	
	function cfgvalListener(){
		$("input[name='cfgval']").blur(function(){
			var value = $(this).val();
			value = value.toLowerCase();
			if(value.indexOf("rgb") > -1){
				var hex = colorRGB2Hex(value);
				$(this).val(hex);
			}
		});
	}
	
	function colorRGB2Hex(colorValue) {
		var rgb = colorValue.split('(')[1].split(')')[0].split(",");
		var r = parseInt(rgb[0]);
		var g = parseInt(rgb[1]);
		var b = parseInt(rgb[2]);
		var hex = "#" + ((1 << 24) + (r << 16) + (g << 8) + b).toString(16).slice(1);
		return hex;
	}
	
	// 初始化运行配置信息
	function initRunningConfig(configData){
		for(var i in configData){
			addOne();
			if(configData[i]["valType"] == "1"){
				$("#config_temp_" + i + " input[name='cfgval']").attr('type','password');
				$("#config_temp_" + i + " input[type='checkbox']").attr("disabled",true);
			}
			for(var j in configData[i]){
				$("#config_temp_" + i).find("input[name='"+j+"']").val(configData[i][j]);
			}
		}
	}
	
	function addOne(){
		var temp = $("#config_temp").clone(true).html();
		$("#configList").append(temp);
		resetTempDivId();
		// cfgkeyListener();
		colorpickerCheckboxEvent();
		passwordCheckboxEvent();
		cfgvalListener();
	}
	
	function removeThis(obj){
		if($("#configList").find("div[itype='config_temp']").length ==1){
		//	return;
		}
		$(obj).parent().parent().remove();
		resetTempDivId();
	}
	
	function resetTempDivId(){
		$("#configList").find("div[itype='config_temp']").each(function(index, dom){
			$(dom).attr("id", "config_temp_"+index).attr("value", index);
		});
	}
	
	//提交
	function submitSave(){
        var configArray = [];
        $("#configList").find("div[itype='config_temp']").each(function(index, dom){
			var config = {}
			$(dom).find("input").each(function(iindex, idom){
				var name = $(idom).attr("name");
				// 去掉运行配置值前后空格-gjw-20190712
				var value = $(idom).val().trim();
				config [name] = value;
			});
			configArray.push(config);
        });
		$.ajax({
	        type: 'post',  
	        url:"${request.contextPath}/serverInfo/saveRunningConfig",
	        datatype:'json',
			data : {id : $("#serverId").val(), configData : JSON.stringify(configArray)},
	        success: function(data){
				data = JSON.parse(data);
				if(data.success){
					parent.opentip("保存成功！");
				}else{
					parent.opentip("保存失败！");
				}
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