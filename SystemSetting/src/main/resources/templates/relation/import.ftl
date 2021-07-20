<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<title>上传流向文件</title><#include "/common/include.ftl" /> 
</head>
<body>
	<form action="${request.contextPath}/relation/upload" method="post" enctype="multipart/form-data">    
	    <input type="file" name="file"/>    
	    <input type="submit"  value="导入"/>     
  	</form>
	<script type="text/javascript">
	
	</script>
</body>
</html>