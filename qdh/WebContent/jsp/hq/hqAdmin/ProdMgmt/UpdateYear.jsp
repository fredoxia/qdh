<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  

<script>
function saveYear(){

	if ($('#updateYearForm').form('validate')){
		var params=$("#updateYearForm").serialize();
	
	    $.post("<%=request.getContextPath()%>/prodOptController/UpdateYear",params, updateYearBKProcess,"json");
	}
	return false;
}
function updateYearBKProcess(data){
	var response = data;
	var returnCode = response.returnCode;
	if (returnCode != SUCCESS)
		$.messager.alert('操作失败', response.msg, 'error');
	else {
		$.modalDialog.handler.dialog('close');
		$("#dataGrid").datagrid('reload');
	}		
}
</script>
<div class="easyui-panel" style="padding:30px 60px;" data-options="fit:true,border:false">
    <form id="updateYearForm" name="updateYearForm" class="easyui-form" method="post" action="UpdateYear" onsubmit="return saveYear();">
	    <div style="margin-bottom:20px"><form:hidden path="year.year_ID"/>
	          <form:input path="year.year" cssClass="easyui-numberspinner" data-options="label:'年份 :',required:true,min:2010,max:2030,editable:false"/>
        </div>
	</form>
</div>