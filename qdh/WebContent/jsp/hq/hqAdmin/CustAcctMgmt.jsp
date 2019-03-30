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
		sortOrder : 'desc',
		nowrap : false,
		singleSelect: true,
		rowStyler: function(index,row){
			var style = "";
			if (row.status == -1)
				style += 'color:red;';

			return style;
		},
		columns : [ [ {
			field : 'custName',
			sortable:true,
			order:'desc',
			title : '客户名字',
			width : 80
		}, {			
			field : 'custRegion',
			title : '客户备注',
			width : 60
		}, {
			field : 'id',
			title : '登录账号',
			width : 50
		}, {			
			field : 'password',
			title : '登录密码',
			width : 50
		}, {
			field : 'statusS',
			title : '状态',
			width : 50,
			formatter : function(value, row, index) {
				var str = '';
				if (row.status == -1){
					str = "被冻结";
				} else if (row.status == 0){
					str = "正常";
				}
				
				return str;
			}
		}, {
			field : 'updateUser',
			title : '操作人员',
			width : 50
		}, {
			field : 'updateDate',
			title : '操作日期',
			sortable:true,
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
function inactiveFun() {
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个客户信息再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
			});
			$.post('<%=request.getContextPath()%>/custAcctController/InactiveCustAcct', {
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
function activeFun() {
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个客户信息再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
			});
			$.post('<%=request.getContextPath()%>/custAcctController/ActiveCustAcct', {
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
function addFun() {
	parent.$.modalDialog({
		id:"addForm",
		title : '添加客户资料',
		width : 500,
		height : 400,
		href : '<%=request.getContextPath()%>/custAcctController/PreAddUpdateCustAcct',
		buttons : [ {
			text : '提交信息',
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form2');
				f.submit();
			}
		} ]
	});
}
function editFun() {
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个客户信息再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	parent.$.modalDialog({
		id:"addForm",
		title : '修改客户资料',
		width : 500,
		height : 400,
		href : '<%=request.getContextPath()%>/custAcctController/PreAddUpdateCustAcct?custId='+id,
		buttons : [ {
			text : '提交信息',
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form2');
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
	$("#status").attr("value", 0);
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
				parent.$.modalDialog({
					title : "客户订单 " + cust.custFullName,
					width : 900,
					height : 550,
					modal : false,
					draggable:true,
					href : '<%=request.getContextPath()%>/custAcctController/OpenCustOrderJSP?' + param
				});

		} else {
			parent.$.messager.alert('失败警告', result.msg, 'error');
		}
	}, 'JSON');
}
function downloadCustInfor(){
    document.searchForm.action="<%=request.getContextPath()%>/custAcctController/HQDownloadCust";
    document.searchForm.submit();
}

function downloadAllCustOrder(){
	$.messager.confirm('功能确认', '你确认需要导出 所有 客户订单?', function(r){
		if (r){
		    document.searchForm.action="<%=request.getContextPath()%>/orderController/HQDownloadAllCustOrder";
		    document.searchForm.submit();;
		}
	});
}

function downloadAllCustOrderForHQ(){
	$.messager.confirm('功能确认', '你确认需要导出 所有 客户订单?', function(r){
		if (r){
		    document.searchForm.action="<%=request.getContextPath()%>/orderController/HQDownloadAllCustOrderForHQOrdering";
		    document.searchForm.submit();;
		}
	});	
}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',border:false" style="height: 55px; overflow: hidden;">
			<form id="searchForm" name="searchForm" method="post">
				<table border="0" class="table table-hover table-condensed" style="display: block;">
					<tr>
						<th>客户名字</th>
						<td><input name="custName" id="custName" placeholder="可以模糊查询客户名字"/></td>
						<th colspan="2"></th>
					</tr>
					<tr>
						<th>客户状态</th>
						<td>
							<select name="status" id="status">
								<option value="-99">所有</option>
								<option value="0" selected>正常状态</option>
								<option value="-1">冻结状态</option>
							</select>
						</td>
						<th></th>
						<td>

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
			<a onclick="searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">搜索客户</a>
			<a onclick="cleanFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">清空查询</a>
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加客户</a>
			<a onclick="editFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-edit'">修改客户</a>
			<a onclick="activeFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-ok'">激活客户</a>
			<a onclick="inactiveFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-no'">冻结客户</a>
			<a onclick="checkOrder();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">打开选中客户订单</a>
			<a onclick="downloadCustInfor();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-dbsave',plain:true">下载客户信息</a>
			<a onclick="downloadAllCustOrder();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-dbsave',plain:true">下载所有客户详细订单</a>
			<a onclick="downloadAllCustOrderForHQ();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-dbsave',plain:true">下载所有客户数量订单</a>
			
			
	</div>


</body>
</html>