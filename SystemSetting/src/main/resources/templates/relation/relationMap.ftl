<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<link rel="stylesheet" href="${request.contextPath}/css/libs/select2.css" type="text/css"/>
<#include "/common/include.ftl" />
<script src="${request.contextPath}/js/select2.min.js"></script>
<script src="${request.contextPath}/js/cytoscape.min.js"></script>
<#-- <script src="${request.contextPath}/js/CytoscapeEdgeEditation.js"></script> -->
<title>流向导图</title>
<style type="text/css">
body { 
  font: 14px helvetica neue, helvetica, arial, sans-serif;
}

#cy {
  height: 85%;
  width: 100%;
  position: absolute;
  left: 0;
  top: 100px;
}
</style>
</head>
<body>
	<div style="display: black;padding:10px 0px 0px 10px; margin-top:5px;" id="btns">
		<div class="clearfix">
		<form class="form-inline" role="form" id="relationForm">
			<div class="form-group form-group-select2">
				<font>服务:</font>
				<select class="" id="serverId" name="serverId"
					 multiple="multiple" style="width: 300px;margin-left: 5px;" maximumSelectionLength="2">
					
				</select>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" onclick="loadMap()">
					<i class=""></i> 加载 
				</button>
			</div>
			
			<div class="form-group form-group-select2">
				<font>源服务功能码:</font>
				<select class="" id="srcServerFc" name="srcServerFc"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group form-group-select2">
				<font>目标服务功能码:</font>
				<select class="" id="destServerFc" name="destServerFc"
					style="width: 180px;margin-left: 5px;">
					
				</select>
			</div>
			<div class="form-group">
				<button class="btn btn-primary btn-xs" type="button" onclick="buildRelation()">
					<i class=""></i> 建立流向 
				</button>
				<button class="btn btn-primary btn-xs" type="button" onclick="removeEdge()">
					<i class=""></i> 删除 
				</button>
			</div>
		</form>
		</div>
	</div>
	<div id="cy"></div>
</body>
<script type="text/javascript">

$(function(){
	serverIdSelect("serverId", "请选择");
	$("#srcServerFc").select2();
	$("#destServerFc").select2();
//	getMapData();
//	init();
});

function serverIdSelect(id, tip){
	var optionHtml = "";
	 $.ajax({
		type:"post",
		url : '${request.contextPath}/serverInfo/getServer',
		async: false,
		success : function(data) {
			 $.each(data,function(i,item){
				 optionHtml+="<option value="+item.id+">"+item.serverName+"</option>";
			 });
		}
	});
	$("#"+id).html(optionHtml);
	$("#"+id).select2({maximumSelectionLength:2});
}

function loadMap(){
	getMapData();
	init();
}

var lineSelectedId = null;
var lineColor = null;
var srcNode = null;
var destNode = null;
var selectColor = "lightgreen";
var split1 = "_HsplitY_";
var split2 = "_HlinetoY_";
var providerMarker = "_HpY_";
var consumerMarker = "_HcY_";
var cy;
function init() {
	cy = cytoscape({

		container: document.getElementById('cy'), // container to render in
	    style: cytoscape.stylesheet()
		.selector('node')
		  .css({
			'content': 'data(name)',
			'border-color' : 'black',
			'font-size':16
		  })
		.selector('edge')
		  .css({
			'curve-style': 'unbundled-bezier',
			'target-arrow-shape': 'triangle',
			'width': 4,
			'line-color': 'data(color)',
			'target-arrow-color': '#000'
		  })
		.selector('.highlighted')
		  .css({
			'background-color': '#61bffc',
			'line-color': '#61bffc',
			'target-arrow-color': '#61bffc',
			'transition-property': 'background-color, line-color, target-arrow-color',
			'transition-duration': '0.5s'
		  }),
		elements: JSON.parse(mapData),
		/*Event : [{
			selector : "edge",
			select : function(evt){
				console.log(evt);
			}
		}],*/
		layout: {
		//	name: 'null',
		//	directed: true
		//	roots: '#a',
		//	margin: 5,
		//	padding: 10
		
		  name: 'breadthfirst',

		  fit: true, // whether to fit the viewport to the graph
		  directed: true, // whether the tree is directed downwards (or edges can point in any direction if false)
		  padding: 5, // padding on fit
		  circle: false, // put depths in concentric circles if true, put depths top down if false
		  spacingFactor: 1.75, // positive spacing factor, larger => more space between nodes (N.B. n/a if causes overlap)
		  boundingBox: undefined, // constrain layout bounds; { x1, y1, x2, y2 } or { x1, y1, w, h }
		  avoidOverlap: true, // prevents node overlap, may overflow boundingBox if not enough space
		  nodeDimensionsIncludeLabels: false, // Excludes the label when calculating node bounding boxes for the layout algorithm
		  roots: undefined, // the roots of the trees
		  maximalAdjustments: 0, // how many times to try to position the nodes in a maximal way (i.e. no backtracking)
		  animate: false, // whether to transition the node positions
		  animationDuration: 500, // duration of animation in ms if enabled
		  animationEasing: undefined, // easing of animation if enabled,
		  animateFilter: function ( node, i ){ return true; }, // a function that determines whether the node should be animated.  All nodes animated by default on animate enabled.  Non-animated nodes are positioned immediately when the layout starts
		  ready: undefined, // callback on layoutready
		  stop: undefined, // callback on layoutstop
		  transform: function (node, position ){ return position; } // transform a given node position. Useful for changing flow direction in discrete layouts
		
		}

	});
	
	cy.on("click", "edge", function(evt) {
		if(evt.target.id().indexOf(split2) > 0){
			if(lineSelectedId != null){
				changeLine(lineColor, "");
			}
			lineSelectedId = evt.target.id();
			lineColor = cy.$('#'+lineSelectedId).style( "line-color");
			changeLine(selectColor, selectColor);
		}
	});
	cy.on("click", "node", function(evt) {
		var nodeId = evt.target.id();
		changeNode(nodeId);
	});
	var resizeViewport = function(){
	//	$("#cy").css("top",$("#btns").height()+10);
		cy.resize();
	};

	$(window).resize(resizeViewport);
	resizeViewport();
  } // end init
  
  function changeNode(nodeId){
	var 
	if(nodeId != srcNode && nodeId != destNode){
		cy.$('#'+lineSelectedId).style( "line-color", lineColor);
	}
	if(nodeId.indexOf(consumerMarker) > 0){
		cy.$('#'+srcNode).style( "background-color", "");
		$("#srcServerFc").val(nodeId).select2();
		srcNode = nodeId;
	}else if(nodeId.indexOf(providerMarker) > 0){
		cy.$('#'+destNode).style( "background-color", "");
		$("#destServerFc").val(nodeId).select2();
		destNode = nodeId;
	}else{
	//	$("#srcServerFc").val("").select2();
	//	$("#destServerFc").val("").select2();
	}
	cy.$('#'+nodeId).style( "background-color", selectColor);
	var lineId = srcNode + split2 + destNode;
	if(cy.$('#'+lineId).isEdge()){
		lineSelectedId = lineId;
		cy.$('#'+lineId).style( "line-color", selectColor);
	}
  }
  
  function changeLine(line_color, node_color){
	cy.$('#'+srcNode).style( "background-color", "");
	cy.$('#'+destNode).style( "background-color", "");
	cy.$('#'+lineSelectedId).style( "line-color", line_color);
	srcNode = lineSelectedId.split(split2)[0];
	destNode = lineSelectedId.split(split2)[1];
	$("#srcServerFc").val(srcNode).select2();
	$("#destServerFc").val(destNode).select2();
	cy.$('#'+srcNode).style( "background-color", node_color);
	cy.$('#'+destNode).style( "background-color", node_color);
  }
  
  var mapData;
  function getMapData(){
	  var serverIds = $("#serverId").select2("val");
	  if(serverIds.length < 1){
		parent.opentip("请选择需要加载的服务！");
		return;
	  }
	  $.ajax({
			type:"post",
			url : "${request.contextPath}/relation/mapData",
			data:{serverIds:JSON.stringify(serverIds)},
			async:false,
			success : function(data) {
				mapData = data.elements;
				iniFcSelect("srcServerFc",data.consumerFcMap);
				iniFcSelect("destServerFc",data.providerFcMap);
			}
	  });
  }
  
  function iniFcSelect(id,data){
    var optionHtml = "";
	for(var i in data){
	    optionHtml+="<option value="+i+">"+data[i]+"</option>";
	}
	$("#"+id).html(optionHtml);
	$("#"+id).select2();
	$("#"+id).val("").select2();
  }
  
  function removeEdge(){
	var srcFc = $("#srcServerFc").select2("val");
	var destFc = $("#destServerFc").select2("val");
	var lineId = srcFc + split2 + destFc;
	if(cy.$('#'+lineId).isEdge()){
		$.ajax({
			type:"post",
			url : "${request.contextPath}/relation/deleteLine",
			data:{lineId : lineId},
			async:true,
			success : function(data) {
				if(data.success){
					cy.$('#'+lineId).remove();
					parent.opentip("删除成功！");
				}else{
					parent.errorMsg("删除成功！");
				}
			}
		});
	}
	
  }
  
  function buildRelation(){
    var srcFc = $("#srcServerFc").select2("val");
	var destFc = $("#destServerFc").select2("val");
	var lineId = srcFc + split2 + destFc;
	var serverIds = $("#serverId").select2("val");
	if(!cy.$('#'+lineId).isEdge()){
		$.ajax({
			type:"post",
			url : "${request.contextPath}/relation/deleteLine",
			data:{srcNode : srcFc, destNode : destFc, serverIds : JSON.stringify(serverIds)},
			async:true,
			success : function(data) {
				if(data.success){
				//	mapData = data.elements;
					cy.add({group: "edges", data: { id: lineId , source: srcFc, target: destFc }});
					parent.opentip("建立流向成功！");
				}else{
					parent.errorMsg(data.msg);
				}
			}
		});
	}
  }
</script>
</html>