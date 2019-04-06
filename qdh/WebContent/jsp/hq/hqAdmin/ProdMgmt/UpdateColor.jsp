<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  

<script>
function updateColor(){
    if ($('#updateColorForm').form('validate')){
    	var params=$("#updateColorForm").serialize();

        $.post("<%=request.getContextPath()%>/prodOptController/UpdateColor",params, updateColorBKProcess,"json");	
	}
}
function updateColorBKProcess(data){
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
    <form id="updateColorForm" name="updateColorForm" method="post"  class="easyui-form" action="action/basicData!saveUpdateColor" onsubmit="return updateColor();">
	    <div style="margin-bottom:20px">
	          <form:hidden path="color.colorId"/>
	          <form:input id="color" path="color.name" cssClass="easyui-textbox" data-options="label:'颜色 : ',required:true,validType:['required','length[1,4]']"/>*</td>
	    </div>
	    </form>
</div>
