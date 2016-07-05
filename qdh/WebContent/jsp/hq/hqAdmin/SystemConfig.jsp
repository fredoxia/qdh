<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../../common/Style.jsp"%>
<script type="text/javascript">
$(document).ready(function(){
	parent.$.messager.progress('close'); 
});

function updateConfig() {
	var params=$("#systemConfForm").serialize();
	$.post('<%=request.getContextPath()%>/systemConfigController/UpdateSystemConfig', params, 
	function(result) {
		if (result.success) {
			$.messager.alert('信息', result.msg, 'info');
		} else if (result.warning){
			$.messager.alert('注意', result.msg, 'warning');
		} else {
			$.messager.alert('失败警告', result.msg, 'error');
		}
	}, 'JSON');
}
function deleteOrderData(){
	$.messager.prompt("密码验证","一旦确认删除,当前系统中客户订货会的数据就将全部清空.输入密码:", function(password){
		if (password == "vj7683c688"){
			var param = "";
			$.post('<%=request.getContextPath()%>/systemConfigController/DeleteCurrentOrderData', param, 
					function(result) {
						if (result.success) {
							$.messager.alert('信息', result.msg, 'info');
						} else {
							$.messager.alert('失败警告', result.msg, 'error');
						}
					}, 'JSON');
		} else {
			alert("密码错误");
		}	   
	});

}
</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true,border:false">

		<div data-options="region:'center',border:false">
		       <form:form id="systemConfForm">  
			    <table class="table table-hover table-condensed" style="display: block; width: 100%; align:left">
					<tr>
						<th height="35" width="10%">能否删除客户</th>
						<td>
							<form:select id="lockUpdateCust" path="lockUpdateCust">
							   <form:option value="0">允许更新客户信息</form:option> 
							   <form:option value="1">不能更新客户信息</form:option> 
							</form:select> 
						</td>
					</tr>
					<tr>
						<th height="35">能否更新品牌</th>
						<td>
							<form:select id="lockUpdateProduct" path="lockUpdateProduct">
							   <form:option value="0">允许更新品牌信息</form:option> 
							   <form:option value="1">不能更新品牌信息</form:option> 
							</form:select> 
						</td>
					</tr>
					<tr>
						<th height="35">是否管理员模式</th>
						<td>
							<form:select id="systemAdminMode" path="systemAdminMode">
							   <form:option value="0">非管理员模式</form:option> 
							   <form:option value="1">管理员模式</form:option> 
							</form:select> * 管理员模式下才能导出单据到条码系统
						</td>
					</tr>	
					<tr>
					<th height="35">订货会代码</th>
						<td>
							<form:input id="orderIdentity" path="orderIdentity" maxlength="15"  class="easyui-numberbox" data-options="min:201606,precision:0"/> 例如: 201604
						</td>
					</tr>						
					<tr>
						<th height="35"></th>
						<td>
							<a onclick="updateConfig();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-edit'">更新系统配置</a>									
						</td>
					</tr>	
					<tr>
						<th colspan="2"><hr/></th>
					</tr>
					<tr>
						<th height="35">系统功能</th>
						<td>
							<a onclick="deleteOrderData();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel'">清空当前客户订单数据</a>									
						</td>
					</tr>					
				</table>
			</form:form>
		</div>

</div>
</body>
</html>