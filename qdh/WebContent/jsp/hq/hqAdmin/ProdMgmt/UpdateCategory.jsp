<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  

<script>
function saveCategory(){
	if ($('#updateCategoryForm').form('validate')){
		var params=$("#updateCategoryForm").serialize();

        $.post("<%=request.getContextPath()%>/prodOptController/UpdateCategory",params, updateCategoryBKProcess,"json");	
	}
}
function updateCategoryBKProcess(data){
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
    <form id="updateCategoryForm" name="updateCategoryForm" class="easyui-form" method="post" action="updateCategory" onsubmit="return saveCategory();">
	    <div style="margin-bottom:20px">
	           <form:hidden path="category.category_ID"/>
	          <form:input id="categoryName" path="category.category_Name"  cssClass="easyui-textbox" data-options="label:'货品类别 : ',required:true,validType:['required','length[1,15]']"/></td>
	    </div>
	</form>
</div>
