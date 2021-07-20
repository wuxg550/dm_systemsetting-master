<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>运行参数配置</title>
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/libs/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/libs/nanoscroller.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/compiled/theme_styles.css" />
<link rel="stylesheet" href="${request.contextPath}/css/compiled/calendar.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${request.contextPath}/css/libs/morris.css" type="text/css" />
<link type="image/x-icon" href="${request.contextPath}/img/favicon.png" rel="shortcut icon" />
</head>
<body>
	<div id="theme-wrapper">
		<header class="navbar" id="header-navbar">
			<div class="container">
				<a href="index.html" id="logo" class="navbar-brand"> 
					运行参数配置
				</a>
				<div class="clearfix">
					<button class="navbar-toggle" data-target=".navbar-ex1-collapse"
						data-toggle="collapse" type="button">
						<span class="sr-only">Toggle navigation</span> <span class="fa fa-bars"></span>
					</button>
					<div class="nav-no-collapse navbar-left pull-left hidden-sm hidden-xs">
						<ul class="nav navbar-nav pull-left">
							<li>
								<a class="btn" id="make-small-nav"> <i class="fa fa-bars"></i></a>
							</li>
						</ul>
					</div>
					<div class="nav-no-collapse pull-right" id="header-nav" style="display: none;">
						<ul class="nav navbar-nav pull-right">
							<li class="dropdown hidden-xs"><a class="btn dropdown-toggle" data-toggle="dropdown"> <i
									class="fa fa-warning"></i> <span class="count">8</span>
									</a>
						    </li>
							<li class="dropdown profile-dropdown">
								<a href="#" class="dropdown-toggle" data-toggle="dropdown"> 
									<img src="img/top.png" alt="" /> <span class="hidden-xs">admin</span> <b class="caret"></b>
								</a>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</header>
		<div id="page-wrapper" class="container">
			<div class="row">
				<div id="nav-col">
					<section id="col-left" class="col-left-nano">
						<div id="col-left-inner" class="col-left-nano-content">
							<div id="user-left-box" class="clearfix hidden-sm hidden-xs" style="display: none;">
								<img alt="" src="img/samples/scarlett-300.jpg" />
								<div class="user-box">
									<span class="name"> 欢迎您<br /> admin
									</span> <span class="status"> <i class="fa fa-circle"></i>
										在线
									</span>
								</div>
							</div>
							<div class="collapse navbar-collapse navbar-ex1-collapse"
								id="sidebar-nav">
								<ul class="nav nav-pills nav-stacked">
									<li><a href="#" class="dropdown-toggle" > <i class="fa fa-tasks"></i> 
										<span>zookeeper服务管理</span> <i class="fa fa-chevron-circle-right drop-icon"></i>
									</a>
										<ul class="submenu">
										<#--	<li><a href="${request.contextPath}/serverList" target="myiframe"> <i class="fa fa-gears"></i> 
											<span>服务列表</span> </a></li> -->
											 <li> <a href="${request.contextPath}/node/nodeList" target="myiframe"><i class="fa fa-database"></i> 
											<span>服务节点管理</span></a></li>
											
										</ul>
									</li>
									<li><a href="#" class="dropdown-toggle"> <i class="fa fa-share-alt"></i> 
											<span>平台服务管理</span> <i class="fa fa-chevron-circle-right drop-icon"></i>
									</a>
										<ul class="submenu">
										    <li><a href="${request.contextPath}/serverType/serverTypeList" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>服务类型</span> </a></li>
											<li><a href="${request.contextPath}/agreement/agreementList" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>协议类型</span> </a></li>
											<li><a default-page href="${request.contextPath}/serverInfo/configServerList" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>配置服务列表</span> </a></li>
											<li><a href="${request.contextPath}/entrance/entranceList" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>配置服务入口列表</span> </a></li>
											<li><a href="${request.contextPath}/serverInfo/commonServerInfo" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>服务公有信息配置</span> </a></li>
										</ul>
									</li>
									<li><a href="#" class="dropdown-toggle"> <i class="fa fa-share-alt"></i> 
											<span>流向管理</span> <i class="fa fa-chevron-circle-right drop-icon"></i>
									</a>
										<ul class="submenu">
										    <li><a href="${request.contextPath}/relation/index" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>流向列表</span> </a></li>
										
										    <li><a href="${request.contextPath}/relation/templet/index" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>模板列表</span> </a></li>
										
										<#--  
										    <li><a href="${request.contextPath}/relation/relationMap" target="myiframe"> <i class="fa fa-cubes"></i> 
											<span>流向导图</span> </a></li> -->
										</ul> 
									</li>
								</ul>
							</div>
						</div>
					</section>
				</div>
				<div id="content-wrapper">
					<div class="row" style="opacity: 1;margin-top: -12px;">
						<div class="col-lg-12" style="padding-left: 0px;">
							<ol class="breadcrumb" id="mds-breadcrumb">
								<li><a href="#" id="seconds">首页</a></li>
								 <li class="active"><span id="second"></span> </li>
							</ol>
						</div>
					</div>
					<div class="row" >
						 <iframe id="myiframe" name="myiframe" src="" width="100%" style="border: 0;"></iframe>
					</div>
					<footer id="footer-bar" class="row">
						<p id="footer-copyright" class="col-xs-12">
							&copy; 浩云科技.
						</p>
					</footer>
				</div>
			</div>
		</div>
	</div>
	 <div id="config-tool" class="closed">
		<a id="config-tool-cog"> <i class="fa fa-cog"></i>
		</a>
		<div id="config-tool-options">
			<h4>布局选项</h4>
			<ul>
				<li>
					<div class="checkbox-nice">
						<input type="checkbox" id="config-fixed-header" /> <label
							for="config-fixed-header"> 固定头部 </label>
					</div>
				</li>
				<li>
					<div class="checkbox-nice">
						<input type="checkbox" id="config-fixed-sidebar" /> <label
							for="config-fixed-sidebar"> 固定左菜单 </label>
					</div>
				</li>
				<li>
					<div class="checkbox-nice">
						<input type="checkbox" id="config-fixed-footer" /> <label
							for="config-fixed-footer"> 固定的页脚 </label>
					</div>
				</li>
				<li>
					<div class="checkbox-nice">
						<input type="checkbox" id="config-boxed-layout" /> <label
							for="config-boxed-layout"> 盒装的布局 </label>
					</div>
				</li>
			</ul>
			<br />
			<h4>皮肤的颜色</h4>
			<ul id="skin-colors" class="clearfix">
				<li>
					<a class="skin-changer" data-skin="" data-toggle="tooltip" style="background-color: #34495e;"></a>
				</li>
				<li>
					<a class="skin-changer" data-skin="theme-white" data-toggle="tooltip" style="background-color: #2ecc71;"> </a>
				</li>
				<li>
					<a class="skin-changer blue-gradient" data-skin="theme-blue-gradient" data-toggle="tooltip" > </a>
				</li>
				<li><a class="skin-changer" data-skin="theme-turquoise" data-toggle="tooltip" 
					style="background-color: #1abc9c;"> </a>
				</li>
				<li><a class="skin-changer" data-skin="theme-amethyst" data-toggle="tooltip" 
					style="background-color: #9b59b6;"> </a>
				</li>
				<li><a class="skin-changer" data-skin="theme-blue" data-toggle="tooltip" 
					style="background-color: #2980b9;"> </a>
				</li>
				<li><a class="skin-changer" data-skin="theme-red" data-toggle="tooltip" 
					style="background-color: #e74c3c;"> </a>
				</li>
				<li><a class="skin-changer" data-skin="theme-whbl" data-toggle="tooltip" 
					style="background-color: #3498db;"> </a>
				</li>
			</ul>
		</div>
	</div> 
	<script src="${request.contextPath}/js/demo-skin-changer.js"></script>
	<script src="${request.contextPath}/js/jquery.js"></script>
	<script src="${request.contextPath}/js/bootstrap.js"></script>
	<script src="${request.contextPath}/js/jquery.nanoscroller.min.js"></script>
	<script src="${request.contextPath}/js/demo.js"></script>
	<script src="${request.contextPath}/js/jquery.slimscroll.min.js"></script>
	<script src="${request.contextPath}/js/raphael-min.js"></script>
	<script src="${request.contextPath}/js/morris.min.js"></script>
	<script src="${request.contextPath}/js/moment.min.js"></script>
	<script src="${request.contextPath}/js/jquery.countTo.js"></script>
	<script src="${request.contextPath}/js/scripts.js"></script>
	<script src="${request.contextPath}/js/plugins/layer/layer.js"></script>
	<script>
	$(document).ready(function() {
		$("#content-wrapper").css({"min-height":window.innerHeight-50,"height":window.innerHeight-50});
		$("#myiframe").height(window.innerHeight-130);
		$(window).bind("resize",function(){
			$("#content-wrapper").css({"min-height":window.innerHeight-50,"height":window.innerHeight-50});
			$("#myiframe").height(window.innerHeight-130);
		});
		
		$(".submenu li").click(function(){
			$("#seconds").text($(this).parents(".submenu").prev("a").find("span").text());
			$("#second").text($(this).find("span").text());
		});
		
		console.log($("a[default-page]"));
		if($("a[default-page]").length > 0){
			$("a[default-page]")[0].click();
		}
	});
	
	function openwin(title, url, width, height, _onModalHide){
		layer.open({
			title: title,
			resize:false,
			skin: 'demo-class',
			area: [width+'px', height+'px'],
		    type: 2, 
		    shift: 1,
		    content: url,//[url, 'no'] //去掉滚动条
		    end:function(){//关闭后回调
	        		if (_onModalHide != null && _onModalHide != undefined) {
	            		_onModalHide.call();
	     			}
	            } 
			}); 
	}

	function opentip(msg){
		layer.msg(msg, {time: 3000, icon:6});
	}

	//错误信息
	function errorMsg(msg){
		 layer.msg(msg, {time: 3000, icon:5});
	}

	function confirms(content,_call,_title){
		if(_title == null || _title == undefined || _title == ""){
		_title = "确认";
		}
       layer.confirm('<div style="color: gray;font-size: 14px;">'+content+'</div>', {
		title:'<div style="font-size: 14px;">'+_title+'</div>',
	    btn: ['确定','取消'], //按钮
    	    shade: 0.3,
    	    shift: 1,
    	    scrollbar: false
    	}, function(index){
    		if(_call != null && _call != undefined){
    			_call.call();
    		}
    		layer.close(index);
    	});
	}
	
	</script>
</body>
</html>