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
			field : 'sumRetailPrice',
			title : '零售金额',
			width : 35
		}, {
			field : 'lastUpdateTime',
			title : '最后下单时间',
			width : 70
		} ] ],
		toolbar : '#toolbar2'
	});
});

function downloadOrder(){
    document.form.action="<%=request.getContextPath()%>/custAcctController/HQDownloadCustOrder";
    document.form.submit();
}

function transferOrder(){
	var toCustId = $("#toCustId").combo("getValue");
	var fromCustId = $("#id").val();
	
	//alert(fromCustId + " , " + toCustId);
	
	if (!isValidInteger(toCustId)){
		alert("请查找正确的客户信息");
		return;
	}
	
	var params = "fromCustId=" + fromCustId + "&toCustId=" + toCustId;
	$.messager.progress({
		title : '提示',
		text : '正在执行数据复制，请稍后....'
	});
	$.post('<%=request.getContextPath()%>/custAcctController/CopyCustOrder', params,  function(result) {
		$.messager.progress('close'); 
		if (result.success) {
			$.messager.alert('消息', result.msg, 'info');	

		} else {
			$.messager.alert('失败警告', result.msg, 'error');
		}
	}, 'JSON');
}
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'center',border:false" title="" style="overflow: hidden;">
			<table id="dataGrid2"></table>
			<form id="form" name="form" method="post">
				<input type="hidden" id="id" name="id" value ="${custId} "/>
			</form>
	</div>
	<div id="toolbar2" style="display: none;">
			<a onclick="downloadOrder();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-redo',plain:true">下载Excel订单</a>
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;

			    <a onclick="transferOrder();" href="javascript:void(0);" class="easyui-linkbutton" data-options="iconCls:'icon-dbsave'">当前订单复制到</a>
			    <form:select id="toCustId" name="toCustId" path="toCustId" items="${customer}" itemLabel="custFullName" itemValue="id" cssClass="easyui-combobox"></form:select>

	</div>

</div>