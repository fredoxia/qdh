<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<!DOCTYPE html>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="../../common/Style.jsp"%>
<script>
$(document).ready(function(){
	parent.$.messager.progress('close'); 
	$('#dg').datagrid({
		toolbar: '#toolbar'
	});
});



function refresh() {
	parent.$.messager.progress({
		title : '提示',
		text : '数据处理中，请稍后....'
	});
	window.location.reload(); 
}

</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'center',border:false">
				<table id="dg" class="easyui-datagrid"  data-options="singleSelect:true,border : false">
					<thead>
					    <tr>
					      <th data-options="field:'0',width:120">客户名字</th>
					      <c:forEach items="${brands}" var="brand">
					         <th data-options="field:'${brand.cbId}',width:100">${brand.brandHeader}</th>
					      </c:forEach>
					      <th data-options="field:'9999',width:120">** 汇总 **</th>
					    </tr>
					</thead>
					<tbody>
					    <c:forEach items="${records}" var="record">
						    <tr>
						      <td>${record.custName}</td>
						      <c:forEach items="${record.orderQ}" var="orderQ">
						        <td>${orderQ}</td>
						      </c:forEach>
						      <td>${record.sumQ}</td>
						    </tr>
					    </c:forEach>
					 </tbody>
				</table>
		</div>
	</div>
	<div id="toolbar" style="display: none;">
			<a onclick="refresh();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'">刷新数据</a>

	</div>
</body>
</html>