<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<script type="text/javascript">
	var dataGrid2;

	$(function() {
		var params= $.serializeObject($('#form2'));
		dataGrid2 = $('#searchCustGrid').datagrid({
			url : '<%=request.getContextPath()%>/custAcctController/SearchJinSuanClients',
			queryParams: params,
			fit : true,
			fitColumns : true,
			border : false,
			pagination : false,
			idField : 'clientId',
			rownumbers:true,
			sortOrder : 'asc',
			nowrap : false,
			singleSelect: false,
			columns : [ [ {
				field : 'clientName',
				title : '客户名字',
				width : 120
			}, {
				field : 'regionName',
				sortable:true,
				order:'desc',
				title : '客户地区',
				width : 120
			}, {
				field : 'action',
				title : '',
				width : 100
			} ] ],
			toolbar : '#toolbar'
		});
	});
	
	function searchFun2() {
		dataGrid2.datagrid('load', $.serializeObject($('#form2')));
	}
	function addCust(){
		var rows = dataGrid2.datagrid('getSelections');
		var dataSize = rows.length;
		
		if (dataSize == 0){
			parent.$.messager.alert('失败警告', "至少选中一条客户数据操作", 'error');
			return false;
		}
		
		var requestIds = "";
		
		for (var i =0; i < dataSize; i++){
			requestIds += rows[i].clientId + ","
		}

		$.post('<%=request.getContextPath()%>/custAcctController/AddCust', {
			"clientIds" : requestIds
		}, function(result) {
			if (result.success) {
				parent.$.messager.alert('提示', result.msg, 'infor');
			} else {
				parent.$.messager.alert('失败警告', result.msg, 'error');
			}
			
			$("#addForm").dialog("close");
			parent.$.modalDialog.openner_dataGrid.datagrid('reload');
		}, 'JSON');
		

		return false;
	}
</script>

<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="height: 25px; overflow: hidden;">
		<form id="form2" method="post" onsubmit="return addCust();">
			客户名字 : <input type="text" id="custName" name="custName"/> <input type="button" value="查询" onclick="searchFun2();"/>
		</form>
	</div>
	<div data-options="region:'center',border:false">
			<table id="searchCustGrid"></table>
	</div>
</div>
