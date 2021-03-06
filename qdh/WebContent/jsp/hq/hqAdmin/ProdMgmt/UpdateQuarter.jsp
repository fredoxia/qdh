<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  

<script>
function saveQuarter(){
	if ($('#updateQuarterForm').form('validate')){
		var params=$("#updateQuarterForm").serialize();

        $.post("<%=request.getContextPath()%>/prodOptController/UpdateQuarter",params, updateQuarterBKProcess,"json");	
	}
}
function updateQuarterBKProcess(data){
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
    <form id="updateQuarterForm" name="updateQuarterForm" method="post" class="easyui-form" action="updateQuarter" onsubmit="return saveQuarter();">
	    <div style="margin-bottom:20px"><form:hidden path="quarter.quarter_ID"/>
	          <form:input id="quarterName" path="quarter.quarter_Name"  cssClass="easyui-textbox" data-options="label:'季度 : ',required:true,validType:['required','length[1,5]']" /></td>
	    </div>
	</form>
</div>
