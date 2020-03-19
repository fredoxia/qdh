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
var dataGrid;

$(function() {

	var params= $.serializeObject($('#searchForm'));
			
	dataGrid = $('#dataGrid').datagrid({
		url : '<%=request.getContextPath()%>/prodOptController/GetBarcodes',
		queryParams: params,
		fit : true,
		fitColumns : true,
		border : false,
		pagination : true,
		pageSize : 40,
		pageList : [ 40, 80,120],
		idField : 'id',
		showFooter:true,
		rownumbers:true,
		nowrap : false,
		singleSelect: true,
		columns : [ [ {
			field : 'year',
			title : '年份',
			width : 20
		}, {
			field : 'quarter',
			title : '季度',
			width : 20
		}, {

			field : 'brand',
			title : '品牌',
			width : 30
		}, {			
			field : 'productCode',
			title : '货号',
			width : 40
		}, {
			field : 'color',
			title : '颜色',
			width : 35
		}, {
			field : 'barcode',
			title : '条码',
			width : 40
		}, {
			field : 'category',
			title : '货品类别',
			width : 40
		}, {
			field : 'unit',
			title : '单位',
			width : 30
		}, {
			field : 'numPerHand',
			title : '每手数量',
			width : 30
		}, {
			field : 'recCost',
			title : '成本',
			width : 30
		}, {
			field : 'wholeSalePrice',
			title : '批发价1',
			width : 30
		}, {
			field : 'salesPriceFactory',
			title : '厂家零售价',
			width : 40
		}, {
			field : 'discount',
			title : '折扣',
			width : 30
		}, {
			field : 'salesPrice',
			title : '连锁店终端价',
			width : 40
		} ] ],
		toolbar : '#toolbar'
	});
});
function searchFun() {
	dataGrid.datagrid('load', $.serializeObject($('#searchForm')));
}
function cleanFun() {

	$("#cbId").attr("value",0);
	dataGrid.datagrid('load', {});
}

</script>
</head>
<body>
<div class="easyui-layout" data-options="fit:true,border:false">
		<div data-options="region:'north',border:false" style="height: 35px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed" style="display: block;">
					<tr>
						<th>品牌条码</th>
						<td>
							<form:select id="cbId" name="cbId" path="cbId" items="${cb}" itemLabel="fullName"  
                               itemValue="id"></form:select> 
						</td>
					</tr>
				</table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<table id="dataGrid"></table>
		</div>
		<div id="toolbar" style="display: none;">
			<a onclick="searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">查询条码</a>
			<a onclick="cleanFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">清空条件</a>
		</div>
</div>
</body>
</html>