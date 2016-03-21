<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
	
	function checkChain(){
		var isChainStore = $("#isChainStore").is(':checked');
		if (isChainStore){
			$("#chainId").combobox("select", -1);
		}
		
		alert(isChainStore + " , "+ $("#chainId").combo("getValue"));
	}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
		<form id="form" method="post">
			<table border="1">
				<tr>
					<th width="80" height="30">连锁店客户</th>
					<td><input type="checkbox" name="isChainStore" id="isChainStore" onclick="checkChain();"/></td>
				</tr>
				<tr>
					<th height="30">散客名字</th>
					<td><input type="text" name="custName" id="custName"/></td>
				</tr>
				
				<tr>
					<th height="30">连锁店客户</th>
					<td>
					   <select id="chainId" name="chainId" class="easyui-combobox">
					      <option value="-1"></option>
						  <c:forEach items="${chainStores}" var="chainStore">
						      <option value="${chainStore.chainId}">${chainStore.chainName}</option>
						  </c:forEach>
					  </select>	
					</td>
				</tr>			
			</table>
		</form>
	</div>
</div>