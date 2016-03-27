<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../../common/Style.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	parent.$.messager.progress('close'); 
});

function exportOrder() {
	var cbId = $("#cbId").val();
	if (cbId == 0)
		parent.$.messager.alert('失败警告', "请选中品牌之后再做导出", 'error');
	else {
	    document.exportForm.action="<%=request.getContextPath()%>/rptController/HQExportFactoryOrder";
	    document.exportForm.submit();
	}
}

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true,border:false">

		<div data-options="region:'center',border:false">
		    <p/>
			<form id="exportForm" name="exportForm">
				<table class="table table-hover table-condensed" style="display: block;">
					<tr>
						<th height="40">品牌条码</th>
						<td>
							<form:select id="cbId" name="cbId" path="cbId" items="${cb}" itemLabel="fullName"  
                               itemValue="id"></form:select> 
						</td>
					</tr>
					<tr>
						<th height="40"></th>
						<td>
							<a onclick="exportOrder();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo'">导出厂家订单</a>
						</td>
					</tr>
				</table>
			</form>
			
		</div>

</div>
</body>
</html>