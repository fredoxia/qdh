<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  

<script>
function saveNumPerHand(){
	if ($('#updateNumPerHandForm').form('validate')){
		var params=$("#updateNumPerHandForm").serialize();
	
	    $.post("<%=request.getContextPath()%>/prodOptController/UpdateNumPerHand",params, updateNumPerHandBKProcess,"json");	
	}
}
function updateNumPerHandBKProcess(data){
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
    <form id="updateNumPerHandForm" name="updateNumPerHandForm" method="post"  class="easyui-form" action="updateNumPerHand" onsubmit="return saveNumPerHand();">
	    <div style="margin-bottom:20px">
	          <form:hidden path="numPerHand.id"/>
	          <form:input path="numPerHand.numPerHand" cssClass="easyui-numberspinner" required="required" data-options="label:'齐码数量:',min:1,max:20,editable:false"/>
	    </div>
	    </form>
</div>
