<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>服务管理列表</title><#include "/common/include.ftl" /> 
</head>
<body>
	<div class="main-box clearfix" style="margin-bottom: 0px;">
	   <div class="jqGrid_wrapper">
           <table id="tableData"></table>
           <div id="tablePager"></div>
       </div>
	</div>
	<div style="display: none;" id="btns">
		<button class="btn btn-primary btn-xs" type="button" id="add-btn" onclick="editAgreementType()">
			<i class="fa  fa-plus"></i> 新增 
		</button>
	</div>
	<script type="text/javascript">
	$(document).ready(function(){
		$.jgrid.defaults.styleUI="Bootstrap";
		$("#tableData").jqGrid({
			url:"${request.contextPath}/agreement/getAllTypePage",
			datatype:"json",
			height: window.innerHeight-heights,
			autowidth:true,
			altRows:true,
			shrinkToFit:true,
			multiselect: false,//复选框   
			rownumbers: true,
			rowNum:rowNums,
			rowList:rowLists,
			colNames:["服务协议类型","服务协议说明","操作"],
			colModel:[
			          {name:"type",index:"type",width:150,sortable:false},
			          {name:"remarks",index:"remarks",width:150},
				      {name:"id",index:"id",width:150,sortable:false,formatter:function(cellvalue, options, rowdata){
				    	  var html='<a href="#" class="table-link" title="编辑" onclick="editAgreementType(\''+cellvalue+'\')"><span class="label label-primary"><i class="fa fa-edit"></i> 编辑</span></a>';
			        		  html+='<a href="#" class="table-link" title="删除" onclick="delForm(\''+cellvalue+'\')"><span class="label label-danger"><i class="fa fa-trash-o"></i> 删除</span></a>';
			        		  return html;
			          }}
			       ],
			pager:"#tablePager",
			viewrecords:true,
			caption:"服务协议列表",
			hidegrid:false
			}).jqGrid('navGrid','#tablePager',{refresh:true,add:false,edit:false,del:false,search:false});
		 $(".ui-jqgrid-caption").html($("#btns").html());
		 $(window).bind("resize",function(){
			var width=$(".jqGrid_wrapper").width();
			$("#tableData").setGridWidth(width);
			$("#tableData").setGridHeight(window.innerHeight-heights);
		 });
	});
	
	function editAgreementType(id){
		var title="新增服务协议";
		var url="${request.contextPath}/agreement/editAgreementType?id=''";
		if(id!=undefined){
			title="编辑服务协议";
			url="${request.contextPath}/agreement/editAgreementType?id="+id;
		}
		parent.openwin(title,url, 600, 500, function(){
			 $("#tableData").trigger("reloadGrid");
		});
	}
	
	function delForm(id){
		parent.confirms("您确定要删除该服务协议类型？",function(){
			 $.ajax({
				type:"post",
				url : '${request.contextPath}/agreement/delete',
				data:{id:id},
				success : function(data) {
					 parent.opentip("删除成功！");
					 $("#tableData").trigger("reloadGrid");
				}
			}); 
		});
	}
	
	
	</script>
</body>
</html>