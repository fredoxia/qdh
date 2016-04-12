<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">
<%@ include file="../common/JQMStyle.jsp"%>
<script>
$(document).ready(function(){
	$("#productCode").focus();
})
function clearProductCode(){
	$("#productCode").focus();
	$("#productCode").attr("value","");
}
</script>
</head>
<body>
	<div id="mainPage" data-role="page">

		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div data-role="content" class="content">
				<table>
				    <tr>
						<td><label for="productCode">货号 : </label></td> 
						<td><input type="text" id="productCode" name="productCode"  placeholder="输入至少三位货号,自动查找"/></td>
					</tr>

				</table>
				<div class="ui-grid-a ui-responsive">
    				<div class="ui-block-a"><input type="button" id="searchBnt" data-theme="a" onclick ="search();" value="查找货品"/></div>
    				<div class="ui-block-b"><input type="button" id="clearBnt" data-theme="b" onclick ="clearProductCode();" value="清空查询条件"/></div>
				</div>

				
		</div>
		<div data-role="footer" data-theme="b" data-position="fixed">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="<%=request.getContextPath()%>/orderController/StartOrder/mobile" data-icon="edit" data-ajax="false" class="ui-btn-active ui-state-persist">我要订货</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRpt/mobile" data-icon="bullets" data-ajax="false">我的订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false">品牌排名</a></li>
		      	<li></li>
		      </ul>
		     </div>
		</div> 

		<jsp:include  page="../common/Popup.jsp"/>
	</div>

</body>
</html>