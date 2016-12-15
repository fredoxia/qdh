<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta http-equiv="expires" content="0"> 
<meta name="viewport" content ="width=device-width, initial-scale=1">

<%@ include file="../common/JQMStyle.jsp"%>
</head>
<body>
	<div id="myOrder" data-role="page">
		<script>
			function changeCategory(categoryId){
				var params = "category_ID=" +categoryId;
				window.location.href = '<%=request.getContextPath()%>/rptController/CustRptByCategory/mobile?' + params;
			}
		</script>
		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div  data-role="content" class="content">
			<form id="form1" name="form1">
		    	<form:select id="categoryId"  path="category.category_ID" items="${categories}" itemLabel="category_Name" itemValue="category_ID" onchange="changeCategory(this.value);"></form:select> 
			</form>
			<table data-role="table" id="table-column-toggle" class="ui-responsive table-stroke">
			     <thead>
			       <tr>
			         <th data-priority="1">货品类别</th>
			         <th>数量(手)</th>
			         <th data-priority="2">总售价</th>
			         <th></th>
			       </tr>
			     </thead>
			     <tbody>
					<c:if test="${fn:length(orderByCategory) == 0}">
						<td colspan="5">暂时还没有订货数据</td>				
					</c:if>
					<c:if test="${fn:length(orderByCategory) > 0 && not empty orderByCategoryFooter}">
						<tr>
							<th>合计</th>
						    <th id="myQ1">${orderByCategoryFooter.quantity}</th>
							<th id="mySum1">${orderByCategoryFooter.sumRetailPrice}</th>
							<th width="27%"></th>
						</tr>
					</c:if>
					<c:forEach items="${orderByCategory}" var="cop">
						<tr id="pRow${cop.pbId}">
						    <td style="vertical-align:middle;">${cop.category}</td>
						    <td style="vertical-align:middle;" id="pQ${cop.copId}">${cop.quantity}</td>
							<td style="vertical-align:middle;" id="pSum${cop.copId}">${cop.sumRetailPrice}</td>
							<td style="vertical-align:middle;">
								<input type="button" value="详细" data-mini="true" onclick="changeCategory(${cop.copId});"/>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(orderByCategory) > 0 && not empty orderByCategoryFooter}">
						<tr>
							<th>合计</th>
						    <th id="myQ1">${orderByCategoryFooter.quantity}</th>
							<th id="mySum1">${orderByCategoryFooter.sumRetailPrice}</th>
							<th width="27%"></th>
						</tr>
					</c:if>
			     </tbody>
			  </table>				
		</div>


		<div data-role="footer" data-theme="b" data-position="fixed" data-tap-toggle="false">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="<%=request.getContextPath()%>/orderController/StartOrder/mobile" data-icon="edit" data-ajax="false">我要订货</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRpt/mobile" data-icon="bullets" data-ajax="false">品牌订单</a></li>
		        <li><a href="<%=request.getContextPath()%>/rptController/CustRptByCategorySum/mobile" data-icon="bars" data-ajax="false" class="ui-btn-active ui-state-persist">类别订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false">品牌排名</a></li>
			</ul>
		     </div>
		</div> 
		<jsp:include  page="../common/Popup.jsp"/>

	</div>

</body>
</html>