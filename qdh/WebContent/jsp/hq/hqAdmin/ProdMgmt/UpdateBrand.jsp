<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<script>
function saveBrand(){
	if ($('#updateBrandForm').form('validate')){
		var params=$("#updateBrandForm").serialize();

    	$.post("<%=request.getContextPath()%>/prodOptController/UpdateBrand",params, updateBrandBKProcess,"json");	
	}
}
function updateBrandBKProcess(data){

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
    <form id="updateBrandForm" name="updateBrandForm" class="easyui-form" method="post" action="action/basicData!saveUpdateBrand"  onsubmit="return saveBrand();">
	    <div style="margin-bottom:20px">
	          <form:hidden path="brand.brand_ID"/>
	          <form:input id="brand_Name" path="brand.brand_Name" cssClass="easyui-textbox" data-options="label:'品牌名称:',required:true,validType:['required','length[1,20]']"/>
	    </div>
	    <div style="margin-bottom:20px">
	          <form:input id="supplier" path="brand.supplier"  cssClass="easyui-textbox" data-options="label:'供应商名字:',required:true,validType:['required','length[1,20]']"/></td>
	    </div>
	 </form>
</div>
