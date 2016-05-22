<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">
<%@ include file="../common/JQMStyle.jsp"%>

</head>
<body>
	<div id="brandRank" data-role="page">
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
				window.location.href = '<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile?' + params;
			}
		</script>
		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div data-role="content" data-id="brc" class="content">
			<form id="form1" name="form1">
		    	<form:select id="cbId"  path="currentBrand.id" items="${cb}" itemLabel="fullName" itemValue="id" onchange="changeCb(this.value);"></form:select> 
			</form>
			<table data-role="table" id="table-column-toggle" data-mode="columntoggle" class="ui-responsive table-stroke" data-column-btn-text="挑选列..">
			     <thead>
			       <tr>
			         <th width="5%" data-priority="1">排名</th>
			         <th>货号</th>
			         <th>品牌</th>
			         <th data-priority="2">零售价</th>
			         <th>总订(手)</th>
			         <th>我订(手)</th>
			         <th width="6%"></th>
			       </tr>
			     </thead>
			     <tbody>
					<c:if test="${fn:length(barcodeRank) == 0}">
						<td colspan="7">暂时还没有汇总数据</td>				
					</c:if>
					<c:forEach items="${barcodeRank}" var="barcodeB">
						<tr>
						    <th style="vertical-align:middle; height:20px">${barcodeB.rank}</th>
						    <td style="vertical-align:middle;">${barcodeB.productCode} ${barcodeB.color}</td>
							<td style="vertical-align:middle;">${barcodeB.brand}</td>
							<td style="vertical-align:middle;">${barcodeB.retailPrice}</td>
							<td style="vertical-align:middle;" id="totalQ${barcodeB.pbId}">${barcodeB.quantity}</td>
							<td style="vertical-align:middle;" id="myQ${barcodeB.pbId}">${barcodeB.myQuantity}</td>
							<td style="vertical-align:middle;"><input type="button" value="加订" data-mini="true" onclick="addOrder(${barcodeB.pbId});"/></td>
						</tr>
					</c:forEach>
			     </tbody>
			  </table>				
		</div>

		<div data-role="footer" data-theme="b" data-position="fixed" data-tap-toggle="false">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="<%=request.getContextPath()%>/orderController/StartOrder/mobile" data-icon="edit" data-ajax="false">我要订货</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRpt/mobile" data-icon="bullets" data-ajax="false">我的订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false" class="ui-btn-active ui-state-persist">品牌排名</a></li>
		      	<li></li>
		      </ul>
		     </div>
		</div> 


	</div>

</body>
</html>