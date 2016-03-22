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
		pagination : false,
		idField : 'id',
		rownumbers:true,
		nowrap : false,
		singleSelect: true,
		columns : [ [ {
			field : 'year',
			title : '年份',
			width : 40,
			formatter: function (value, row, index){
				return row.product.year.year;
			}
		}, {
			field : 'quarter',
			title : '季度',
			width : 80,
			formatter: function (value, row, index){
				return row.product.quarter.quarter_Name;
			}
		}, {
			field : 'brand',
			title : '品牌',
			width : 50,
			formatter: function (value, row, index){
				return row.product.brand.brand_Name;
			}
		}, {			
			field : 'barcode',
			title : '条码',
			width : 50
		}, {

			field : 'productCode',
			title : '货号',
			width : 50,
			formatter: function (value, row, index){
				return row.product.productCode;
			}
		}, {
			field : 'createDate',
			title : '条码系统创建日期',
			sortable:true,
			order:'desc',
			width : 80
		}, {
			field : 'action',
			title : '',
			width : 200
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
		<div data-options="region:'north',title:'查询条件',border:false" style="height: 50px; overflow: hidden;">
			<form id="searchForm">
				<table class="table table-hover table-condensed" style="display: block;">
					<tr>
						<th>品牌条码</th>
						<td>
							<form:select name="cbId" path="cbId" items="${cb}" itemLabel="fullName"  
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
			<a onclick="searchFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-search',plain:true">过滤条件</a>
			<a onclick="cleanFun();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-cancel',plain:true">清空条件</a>
	</div>
</div>
</body>
</html>