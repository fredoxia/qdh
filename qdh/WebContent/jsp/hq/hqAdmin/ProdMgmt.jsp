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
		url : '<%=request.getContextPath()%>/prodOptController/GetCurrentBrands',
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
			field : 'year',
			title : '年份',
			width : 50
		}, {
			field : 'quarter',
			title : '季度',
			width : 50
		}, {
			field : 'brand',
			title : '品牌',
			width : 50
		}, {
			field : 'numOfBarcodes',
			title : '条码数',
			width : 40
		}, {
			field : 'updateUser',
			title : '操作人员',
			width : 50
		}, {
			field : 'updateDate',
			title : '导入时间',
			width : 90
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
		parent.$.messager.alert('错误', '请选中一个品牌再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	parent.$.messager.confirm('询问', '您确定要从订货系统中删除当前选中季度的品牌？', function(b) {
		if (b) {
			parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
			});
			$.post('<%=request.getContextPath()%>/prodOptController/DeleteCurrentBrand', {
				currentBrandId : id
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
		title : '导入品牌',
		width : 500,
		height : 200,
		href : '<%=request.getContextPath()%>/prodOptController/PreAddCurrentBrand',
		buttons : [ {
			text : '导入',
			handler : function() {
				parent.$.modalDialog.openner_dataGrid = dataGrid;
				var f = parent.$.modalDialog.handler.find('#form');
				f.submit();
			}
		} ]
	});
}

function reloadFun(){
	var rows = dataGrid.datagrid('getSelections');
	if (rows.length == 0){
		parent.$.messager.alert('错误', '请选中一个品牌再继续操作', 'error');
		return;
	}
	
	var	id = rows[0].id;
	
	parent.$.messager.confirm('询问', '该功能是更正某些修改的标签信息，耗时会比较长,继续？', function(b) {
		if (b) {
			parent.$.messager.progress({
					title : '提示',
					text : '数据处理中，请稍后....'
			});
			$.post('<%=request.getContextPath()%>/prodOptController/UpdateCurrentBrand', {
				currentBrandId : id
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
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">导入新品牌</a>
			<a onclick="deleteFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除品牌</a>
			<a onclick="reloadFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'">更新品牌</a>
	</div>


</body>
</html>