<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<div data-role="header" data-theme="b">
      <a href="<%=request.getContextPath()%>/jsp/chainOrder/Logout.jsp" data-rel="dialog" data-transition="fade">退出</a>
	  <h4><c:out value="${sessionScope.HQ_SESSION_INFO.userName}" escapeXml="false" default="未登陆"></c:out></h4>
</div> 