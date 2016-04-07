<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<div data-id="footer1" data-role="footer" data-theme="b" data-position="fixed">
	<div data-role="navbar">
      <ul>
      	<li><a href="logout" data-icon="bullets">我的订单</a></li>
      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star" data-transition="fade">品牌排名</a></li>
      	<li></li>
      	<li></li>
      </ul>
     </div>
</div> 