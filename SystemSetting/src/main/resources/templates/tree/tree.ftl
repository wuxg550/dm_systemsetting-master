<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>数据节点</title> <#include "/common/include.ftl" />
<script src="${request.contextPath}/js/plugins/jsTree/jstree.min.js"></script>
<link href="${request.contextPath}/js/plugins/jsTree/themes/default/style.min.css" rel="stylesheet">
<style type="text/css">
.font_left {
  margin-left: 30px;
}
.font_size{
  font-size: 1.5em;
}
</style>
</head>
<body style="background-color: #f1f3f7;">
	<div class="row" style="opacity: 1;">
		<div class="row" id="serverlist">
		</div>
		<div>
			<div class="row">
				<div class="col-lg-5">
					<div class="main-box clearfix">
						<header class="main-box-header clearfix"><h2>节点树</h2></header>
						<div class="main-box-body clearfix">
							<div class="conversation-wrapper">
								<div class="conversation-content">
									<div class="conversation-inner">	
										<div class="main-box clearfix" style="margin-bottom: 0px;">
											<div id="jstree">
											</div>
										</div>
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="col-lg-6">
					<div class="main-box clearfix">
						<header class="main-box-header clearfix"><h2>节点详情</h2></header>
						<div class="main-box-body clearfix">
							<div class="conversation-wrapper">
								<div class="conversation-content">
									<div class="conversation-inner">	
									</div>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
<script>
$(function(){
	$.ajax({
		type:'post',
		data:'',
		url:'${request.contextPath}/tree/getAllServer',
		async:false,
		success:function(data){
			var html = "";
			$.each(data,function(i,item){
				var classType = "";
				if(item.status == 'ONLINE'){
					classType = "green-bg";
				} else if (item.status == 'OFFLINE'){
					classType = "red-bg";
				} else {
					classType = "red-bg";
				}
			  html += '<div class="col-md-3 col-sm-4 col-xs-10" style="cursor: pointer;" onclick="getServerTree(this)">'+
					 '<div class="main-box small-graph-box '+classType+'">'+
					 '<span class="subinfo">'+
						'<span class="social-count font_size">ip</span>'+
						'<span class="social-action font_left font_size" name="ip">'+item.ip+'</span>'+
						'<span class="social-action font_left font_size" name="status" style="display:none;">'+item.status+'</span>'+
					 '</span>'+
					 '<span class="subinfo">'+
						'<span class="social-count font_size">端口</span>'+
						'<span class="social-action font_left font_size" name="port">'+item.port+'</span>'+
					' </span>'+
					 '</div>'+
					 '</div>'
			});
			$("#serverlist").html(html);
		}
		
	});
	
	/* $("#serverlist").find('div').each(function(){
		$(this).click(function(){
			$(this).find("span").each(function(){
				if($(this).attr("name") == "ip"){
					alert($(this).html());
				}
			});
		});
	}); */
	
	
	
	
	 /*   */
	 
	  
});

function getServerTree(htmlObj){
	var ip = "";
	var port = "";

	if($(htmlObj).find("span[name='status']").html() == "ONLINE"){
		ip = $(htmlObj).find("span[name='ip']").html();
		port = $(htmlObj).find("span[name='port']").html();
		$('#jstree').data('jstree', false).empty();
		$("#jstree").jstree({
	           "core": {
	              //'initially_open':["abc"],
	               'data': {
	                   'url': '${request.contextPath}/tree/getTree',
	                   'dataType': 'json',
	                   'data': function (node) { // 传给服务端点击的节点
	                       return { path: node.id == "#" ? "" : node.id,ip:ip,port:port };
	                   },
	               }
	           }
	      }).bind('activate_node.jstree', function(obj,e) {   
	    	  var currentNode = e.node;
	         alert(currentNode.id);    
	    });
	}else if($(htmlObj).find("span[name='status']").html() == "OFFLINE"){
		parent.opentip("该服务不在线！");
		return;
	}
	
	
}

</script>
</html>