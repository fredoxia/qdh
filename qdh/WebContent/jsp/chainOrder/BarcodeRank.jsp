<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">
<%@ include file="../common/JQMStyle.jsp"%>
<script>
$(document).ready(function(){
	
});

</script>
</head>
<body>
	<div id="brandRank" data-role="page">

		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div data-role="content" class="content">

			<table data-role="table" id="table-column-toggle" data-mode="columntoggle" class="ui-responsive table-stroke" data-column-btn-text="挑选列..">
			     <thead>
			       <tr>
			         <th data-priority="1">排名</th>
			         <th>货号</th>
			         <th>品牌</th>
			         <th data-priority="2">发价</th>
			         <th>所有订货</th>
			         <th>我的订货</th>
			       </tr>
			     </thead>
			     <tbody>
					<c:if test="${fn:length(barcodeRank) == 0}">
						<td colspan="6">暂时还没有汇总数据</td>				
					</c:if>
					<c:forEach items="${barcodeRank}" var="barcodeB">
						<tr>
						    <th>${barcodeB.rank}</th>
						    <td>${barcodeB.productCode} ${barcodeB.color}</td>
							<td>${barcodeB.brand}</td>
							<td>${barcodeB.wholePrice}</td>
							<td>${barcodeB.quantity}</td>
							<td>${barcodeB.myQuantity}</td>
						</tr>
					</c:forEach>
			     </tbody>
			  </table>				
		</div>

		<jsp:include  page="../common/MobileFooter.jsp"/>

		<jsp:include  page="../common/Popup.jsp"/>

	</div>

</body>
</html>