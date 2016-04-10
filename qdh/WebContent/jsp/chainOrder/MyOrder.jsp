<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<%@ include file="../common/JQMStyle.jsp"%>
</head>
<body>
	<div id="myOrder" data-role="page">
		<script>
			function addOrder(pbId){
				$.mobile.loading("show",{ theme: "b", text: "正在加载数据", textonly: false});
				var params="pbId=" + pbId;

				$.post('<%=request.getContextPath()%>/orderController/OrderMore/mobile', params, 
				function(result) {
					$.mobile.loading("hide");
					if (result.success) {
						var resultData = result.obj;
						var myQ = resultData.myQ;
						var totalQ = resultData.totalQ;
						$("#myQ" + pbId).html(myQ);
						$("#totalQ" + pbId).html(totalQ);
					} else {
						renderPopup("系统错误",result.msg)
					}
				}, 'JSON');
			}
			function changeCb(changeVal){
				var params = "id=" +changeVal;
				window.location.href = '<%=request.getContextPath()%>/rptController/CustRpt/mobile?' + params;
			}
		</script>
		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div class="easyui-layout" data-options="fit:true,border:false">
			<form id="form1" name="form1">
		    	<form:select id="cbId"  path="currentBrand.id" items="${cb}" itemLabel="fullName" itemValue="id" onchange="changeCb(this.value);"></form:select> 
			</form>
			<table data-role="table" id="table-column-toggle" class="ui-responsive table-stroke">
			     <thead>
			       <tr>
			         <th>品牌</th>
			         <th>货号</th>
			         <th>数量(手)</th>
			         <th>总价</th>
			       </tr>
			     </thead>
			     <tbody>
					<c:if test="${fn:length(cops) == 0}">
						<td colspan="7">暂时还没有订货数据</td>				
					</c:if>
					<c:if test="${fn:length(cops) > 10 && not empty copsFooter}">
						<tr>
							<th colspan="2">当前合计</th>
						    <th>${copsFooter.quantity}</th>
							<th>${copsFooter.sumWholePrice}</th>
						</tr>
					</c:if>
					<c:forEach items="${cops}" var="cop">
						<tr>
							<td style="vertical-align:middle;">${cop.brand}</td>
						    <td style="vertical-align:middle;">${cop.productCode} ${cop.color}</td>
						    <td style="vertical-align:middle;" id="myQ${barcodeB.pbId}">${cop.quantity}</td>
							<td style="vertical-align:middle;">${cop.sumWholePrice}</td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(cops) > 0 && not empty copsFooter}">
						<tr>
							<th colspan="2">当前合计</th>
						    <th>${copsFooter.quantity}</th>
							<th>${copsFooter.sumWholePrice}</th>
						</tr>
					</c:if>
			     </tbody>
			  </table>				
		</div>


		<div data-role="footer" data-theme="b" data-position="fixed">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="<%=request.getContextPath()%>/orderController/StartOrder/mobile" data-icon="edit" data-ajax="false">我要订货</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRpt/mobile" data-icon="bullets" data-ajax="false" class="ui-btn-active ui-state-persist">我的订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false">品牌排名</a></li>
		      	<li></li>
		      </ul>
		     </div>
		</div> 


	</div>

</body>
</html>