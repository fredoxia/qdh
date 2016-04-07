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
	
});

</script>
</head>
<body>
	<div id="mainPage" data-role="page">

		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div data-role="content" class="content">

			<p style="">
				登录系统2
			</p>
			
		</div>

		<div data-role="footer" data-theme="b" data-position="fixed">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="logout" data-icon="bullets" class="ui-btn-active ui-state-persist">我的订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false">品牌排名</a></li>
		      	<li></li>
		      	<li></li>
		      </ul>
		     </div>
		</div> 

		<jsp:include  page="../common/Popup.jsp"/>

	</div>

</body>
</html>