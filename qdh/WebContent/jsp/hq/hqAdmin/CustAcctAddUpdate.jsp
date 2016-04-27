<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<script type="text/javascript">
	$(function() {
		parent.$.messager.progress('close');
		$('#form').form({
			url : '<%=request.getContextPath()%>/custAcctController/AddUpdateCustAcct',
			onSubmit : function() {
				return validateAddForm();
			},
			success : function(result) {
				parent.$.messager.progress('close');
				result = $.parseJSON(result);
				if (result.success) {
					parent.$.modalDialog.openner_dataGrid.datagrid('reload');//之所以能在这里调用到parent.$.modalDialog.openner_dataGrid这个对象，是因为user.jsp页面预定义好了
					parent.$.modalDialog.handler.dialog('close');
				} else {
					parent.$.messager.alert('错误', result.msg, 'error');
				}
			}
		});
	});
	
	function validateAddForm(){
		var error = "";
		var chain = $("#chainId").combo("getValue");
		var custName = $.trim($("#custName").val());
		var chainTxt = $("#chainId").combo("getText");
		
		if ((!isValidPositiveInteger(chain) && chain!=-1) || chain == chainTxt) 
	        error += "连锁店客户请从下拉菜单中选取，请检查\n";
	    else if (chain == -1 && custName == "")
			error = "散客 必须输入客户名字<br/>连锁店客户 请选择连锁店下拉";
		
		
		if (error == ""){
			parent.$.messager.progress({
				title : '提示',
				text : '数据处理中，请稍后....'
			});
			return true;

		} else {
			parent.$.messager.alert('错误', error, 'error');
			return false;
		}
	}
	
	var dataGrid2;

	$(function() {
		dataGrid2 = $('#searchCustGrid').datagrid({
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
</script>
<div class="easyui-layout" data-options="fit:true,border:false">
	<div data-options="region:'north',border:false" title="" style="height: 45px; overflow: hidden;">
		<form id="form" method="post">
			客户名字 : <form:input id="custName" path="cust.custName"/> <input type="button" value="查询"/>
		</form>
	</div>
	<div data-options="region:'center',border:false">
			<table id="searchCustGrid"></table>
	</div>
</div>