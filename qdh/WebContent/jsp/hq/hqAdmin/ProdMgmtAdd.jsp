<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form').form({
			url : '<%=request.getContextPath()%>/prodOptController/AddCurrentBrand',
			onSubmit : function() {
				return validateAddForm();
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
	
	function validateAddForm(){
		var error = "";
		var yearId = $("#yearId").val();
		var quarterId = $("#quarterId").val();
		var brandId = $("#brandId").combo("getValue");
		var brandTxt = $("#brandId").combo("getText");

		
		if (yearId == ""){
			error += "年份 - 是必选项\n";
		}
		if (quarterId == ""){
			error += "季度 - 是必选项\n";
		}
		if (brandId == -1){
	          error += "品牌 - 是必选项\n";
		} else if (!isValidPositiveInteger(brandId) || brandId == brandTxt) {
	        error += "品牌 - 必须是系统已经存在的，请检查\n";
		}
		if (error == ""){
			if (confirm("你确定要导入当前品牌")){
				parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
				});
				return true;
			} else 
				return false ;
		} else {
			parent.$.messager.alert('错误', error, 'error');
			return false;
		}
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table>
				<tr>
					<th width="50px" height="80px">年份</th>
					<td>
					  <select id="yearId" name="yearId">
						  <c:forEach items="${year}" var="year">
						      <option value="${year.year_ID}">${year.year}</option>
						  </c:forEach>
					  </select>
					</td>
					<th width="50px">季度</th>
					<td>
					  <select id="quarterId" name="quarterId">
						  <c:forEach items="${quarter}" var="quarter">
						      <option value="${quarter.quarter_ID}">${quarter.quarter_Name}</option>
						  </c:forEach>
					  </select>					
					</td>
					<th width="50px">品牌</th>
					<td>
					  <select id="brandId" name="brandId" class="easyui-combobox">
					      <option value="-1"></option>
						  <c:forEach items="${brand}" var="brand">
						      <option value="${brand.brand_ID}">${brand.brand_Name}</option>
						  </c:forEach>
					  </select>	
					</td>
				</tr>
			</table>
		</form>
	</div>
</div>