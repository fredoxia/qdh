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
			$('#menu').menu('show', {
				left : e.pageX,
				top : e.pageY
			});
		}
	});
});
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
			<a onclick="addFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">导入品牌</a>
			<a onclick="batchDeleteFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-remove'">删除品牌</a>
	</div>


</body>
</html>