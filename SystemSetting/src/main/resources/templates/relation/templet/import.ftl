<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>上传流向模板文件</title><#include "/common/include.ftl" /> 
</head>
<body>
	<form id="centerForm" action="" method="post" enctype="multipart/form-data">    
	    <input  accept=".xls" type="file" name="file" multiple=true />    
	    <input type="button"  value="导入"  onclick="importTemplet()"/>     
  	</form>
	<script type="text/javascript">
		function importTemplet(){
			$("#centerForm").ajaxSubmit({
				type: 'post',  
				url:"${request.contextPath}/relation/templet/uploadExcel",
			//    datatype:'multipart/form-data',
				success: function(data){
					if(data.success){
						parent.opentip(data.msg);
					}else{
						parent.errorMsg(data.msg);
					} 
				}
			});
		}
	</script>
</body>
</html>