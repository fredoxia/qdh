<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../../common/Style.jsp"%>
<script>
$(document).ready(function(){
	parent.$.messager.progress('close'); 
});

var dataGrid;

$(function() {
	dataGrid = $('#dataGrid').datagrid({
		url : '<%=request.getContextPath()%>/custAcctController/GetCustAccts',
		fit : true,
		fitColumns : true,
		border : false,
		pagination : false,
		idField : 'id',
		sortName : 'updateDate',
		rownumbers:true,
		sortOrder : 'asc',
		nowrap : false,
		singleSelect: true,
		columns : [ [ {
			field : 'custName',
			sortable:true,
			order:'desc',
			title : '客户名字',
			width : 80
		}, {
			field : 'chainStoreName',
			sortable:true,
			order:'desc',
			title : '连锁店名字',
			width : 80
		}, {
			field : 'id',
			title : '登录账号',
			width : 50
		}, {			
			field : 'password',
			title : '登录密码',
			width : 50
		}, {

			field : 'updateUser',
			title : '操作人员',
			width : 50
		}, {
			field : 'updateDate',
			title : '操作日期',
			width : 80
		}, {
			field : 'action',
			title : '',
			width : 200
		} ] ],
		toolbar : '#toolbar',
		onRowContextMenu : function(e, rowIndex, rowData) {
			e.preventDefault();
			$(this).datagrid('unselectAll').datagrid('uncheckAll');
			$(this).datagrid('selectRow', rowIndex);
		}
	});
});
function deleteFun() {
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个客户信息再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	parent.$.messager.confirm('询问', '删除客户信息会一并删除当前客户的订单,确认当前客户没有遗留订单？', function(b) {
		if (b) {
			parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
			});
			$.post('<%=request.getContextPath()%>/custAcctController/DeleteCustAcct', {
				id : id
			}, function(result) {
				if (result.success) {
					parent.$.messager.alert('成功提示', result.msg, 'info');
					dataGrid.datagrid('reload');
				} else {
					parent.$.messager.alert('失败警告', result.msg, 'error');
				}
				parent.$.messager.progress('close');
			}, 'JSON');
		}
	});
}
function addFun() {
	parent.$.modalDialog({
		title : '添加客户',
		width : 500,
		height : 200,
		href : '<%=request.getContextPath()%>/custAcctController/PreAddUpdateCustAcct',
		buttons : [ {
			text : '提交信息',
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		} ]
	});
}
function updateFun(){
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个客户信息再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	parent.$.modalDialog({
		title : '修改客户',
		width : 500,
		height : 200,
		href : '<%=request.getContextPath()%>/custAcctController/PreAddUpdateCustAcct?id=' + id,
		buttons : [ {
			text : '提交信息',
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		} ]
	});
}
function searchFun() {
	dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function cleanFun() {
	$('#searchForm input').val('');
	$("#custType").attr("value", -1);
	dataGrid.datagrid('load', {});
}
function checkOrder(){
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个客户信息再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	$.post('<%=request.getContextPath()%>/custAcctController/CheckCustOrder', {
		id : id
	}, function(result) {
		if (result.success) {
				var param = "id="+ id;
				var cust = result.obj;
				$.modalDialog({
					title : "客户订单 " + cust.custName + " " + cust.chainStoreName,
					width : 900,
					height : 500,
					modal : false,
					draggable:true,
					href : '<%=request.getContextPath()%>/custAcctController/OpenCustOrderJSP?' + param
				});

		} else {
			parent.$.messager.alert('失败警告', result.msg, 'error');
		}
	}, 'JSON');
}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 90px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed" style="display: block;">
					<tr>
						<th>客户名字</th>
						<td><input name="custName" id="custName" placeholder="可以模糊查询客户名字"/></td>
					</tr>
					<tr>
						<th>客户种类</th>
						<td>
							<select name="custType" id="custType">
								<option value="-1">所有</option>
								<option value="1">连锁店客户</option>
								<option value="2">零散客户</option>
							</select>
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加客户</a>
			<a onclick="updateFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改客户</a>
			<a onclick="deleteFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除客户</a>
			<a onclick="searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索客户</a>
			<a onclick="cleanFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">清空条件</a>
			<a onclick="checkOrder();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">打开订单</a>
	</div>


</body>
</html>