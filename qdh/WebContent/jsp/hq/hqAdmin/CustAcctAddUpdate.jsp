<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form').form({
			url : '<%=request.getContextPath()%>/custAcctController/AddUpdateCustAcct',
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
		var chain = $("#chainId").combo("getValue");
		var custName = $.trim($("#custName").val());
		var chainTxt = $("#chainId").combo("getText");
		
		if ((!isValidPositiveInteger(chain) && chain!=-1) || chain == chainTxt) 
	        error += "连锁店客户请从下拉菜单中选取，请检查\n";
	    else if (chain == -1 && custName == "")
			error = "散客 必须输入客户名字<br/>连锁店客户 请选择连锁店下拉";
		
		
		if (error == ""){
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			return true;

		} else {
			parent.$.messager.alert('错误', error, 'error');
			return false;
		}
	}
	

</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table border="0">
				<form:hidden id="id" path="cust.id"/>
			   <tr>
					<th height="30">客户名字</th>
					<td><form:input id="custName" path="cust.custName"/> *散客必须输入，方便以后查找</td>
				</tr>

				<tr>
					<th width="80" height="30">连锁店客户</th>
					<td>
					      <form:select path="cust.chainId"  cssClass="easyui-combobox" items="${chainStores}" itemLabel="chainName"  
                               itemValue="chainId"></form:select> 
					</td>
				</tr>
		   </table>
		</form>
	</div>
</div>