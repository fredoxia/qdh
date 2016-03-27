<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<script type="text/javascript">
var dataGrid2;

$(function() {
	var params= $.serializeObject($('#form'));
	dataGrid2 = $('#dataGrid2').datagrid({
		url : '<%=request.getContextPath()%>/custAcctController/GetCustOrderData',
		fit : true,
		queryParams: params,
		fitColumns : true,
		border : false,
		showFooter : true,
		rownumbers:true,
		nowrap : false,
		singleSelect: true,
		columns : [ [ {
			field : 'year',
			title : '年份',
			width : 30
		}, {
			field : 'quarter',
			title : '季度',
			width : 30
		}, {
			field : 'brand',
			title : '品牌',
			width : 50
		}, {
			field : 'productCode',
			title : '货号',
			width : 50
		}, {
			field : 'color',
			title : '颜色',
			width : 40
		}, {
			field : 'barcode',
			title : '条码',
			width : 50
		}, {
			field : 'quantity',
			title : '数量(手)',
			width : 35
		}, {
			field : 'sumWholePrice',
			title : '金额',
			width : 35
		}, {
			field : 'lastUpdateTime',
			title : '最后下单时间',
			width : 70
		} ] ]
	});
});
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<form id="form" method="post">
			    <input type="hidden" id="id" name="id" value ="${custId} "/>
			</form>
			<table id="dataGrid2"></table>
	</div>
</div>