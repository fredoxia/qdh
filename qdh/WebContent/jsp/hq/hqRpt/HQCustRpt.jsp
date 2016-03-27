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
	var params= $.serializeObject($('#searchForm'));
	
	dataGrid = $('#dataGrid').datagrid({
		url : '<%=request.getContextPath()%>/rptController/GenerateHQCustRpt',
		queryParams: params,
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		pageSize : 20,
		pageList : [ 20, 40, 60],
		showFooter:true,
		rownumbers:true,
		nowrap : false,
		singleSelect: true,
		columns : [ [ {
			field : 'custName',
			title : '客户名字',
			width : 30
		}, {
			field : 'chainStoreName',
			title : '连锁店',
			width : 45
		}, {		
			field : 'quantity',
			title : '订货量 (手)',
			width : 30,
			styler: function(value,row,index){
				return 'font-weight:bold;color:red;font-size:large';
			}
		}, {
			field : 'sumWholeSalePrice',
			title : '成交金额',
			width : 30,
			styler: function(value,row,index){
				return 'font-weight:bold;color:red;font-size:large';
			}
		}, {
			field : 'action',
			title : '',
			width : 150
		} ] ],
		toolbar : '#toolbar'
	});
});

var autoRefresher = null;

function refresh() {
	clearInterval(autoRefresher);
	dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function autoRefresh(){
	
	$.messager.prompt("输入","请输入需要多少秒自动刷新报表数据? 默认20秒 <br>(重新点击手动刷新会取消自动刷新)", function(second){
		var interval = 20;
		if (second != "" && isValidInteger(second)==true){
			if (second < 10)
				interval = 20;
			else 
		   		interval = second;
		}	
		clearInterval(autoRefresher);
		autoRefresher = setInterval("dataGrid.datagrid('load', $.serializeObject($('#searchForm')))", 1000 * interval);
	   
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
			<a onclick="refresh();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'">手动刷新数据</a>
			<a onclick="autoRefresh();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-syn_data'">自动刷新数据</a>
	</div>
</body>
</html>