<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/bootstrap/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/libs/font-awesome.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/libs/nanoscroller.css" />
<link rel="stylesheet" type="text/css" href="${request.contextPath}/css/compiled/theme_styles.css" />
<link rel="stylesheet" href="${request.contextPath}/css/compiled/calendar.css" type="text/css" media="screen" />
<link rel="stylesheet" href="${request.contextPath}/css/libs/morris.css" type="text/css" />
<link href="${request.contextPath}/js/plugins/jqgrid/ui.jqgridffe4.css?0820" rel="stylesheet">
<link rel="stylesheet" href="${request.contextPath}/css/libs/select2.css" type="text/css"/>

<script src="${request.contextPath}/js/jquery.js"></script>
<script src="${request.contextPath}/js/jquery.form.js"></script>
<script src="${request.contextPath}/js/bootstrap.js"></script>
<script src="${request.contextPath}/js/bootstrapValidator.min.js"></script>
<script src="${request.contextPath}/js/jquery.nanoscroller.min.js"></script>
<script src="${request.contextPath}/js/plugins/jqgrid/i18n/grid.locale-cnffe4.js?0820"></script>
<script src="${request.contextPath}/js/plugins/jqgrid/jquery.jqGrid.minffe4.js?0820"></script>
<script src="${request.contextPath}/js/select2.min.js"></script>
<script>
var rowNums=20;
var rowLists = [10,20,30,50];
var heights=143;
$.ajaxSetup({headers : {"X-CSRF-TOKEN":"${_csrf.token}"}});
</script>

<style type="text/css">
#centerForm .help-block {
margin-bottom: -6px;
}
</style>