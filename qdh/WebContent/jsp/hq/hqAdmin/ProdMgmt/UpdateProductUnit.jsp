<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  

<script>
function saveProductUnit(){
	if ($('#updateUnitForm').form('validate')){
		var params=$("#updateUnitForm").serialize();

    	$.post("<%=request.getContextPath()%>/prodOptController/UpdateProductUnit",params, updateUnitBKProcess,"json");	
	}
}
function updateUnitBKProcess(data){
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
    <form id="updateUnitForm" name="updateUnitForm" class="easyui-form" method="post" action="updateProductUnit"  onsubmit="return saveProductUnit();">
	    <div style="margin-bottom:20px">
	          <form:hidden path="productUnit.id"/>
	          <form:input path="productUnit.productUnit"  cssClass="easyui-textbox" data-options="label:'货品单位  : ',required:true,validType:['required','length[1,3]']"/></td>
	    </div>
	</form>
</div>