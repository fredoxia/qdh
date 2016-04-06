<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">
<%@ include file="../common/JQMStyle.jsp"%>

</head>
<body>
	<div id="logoutPage" data-history="false" data-role="page" data-close-btn="right" >
	
		<div data-role="header" data-theme="b">
			<h1>退出系统</h1>
		</div>
		<div data-role="content">
			<p>完成订货,确定退出系统?</p>
			<a href="#" data-role="button" data-theme="c" onclick="logout();">确定</a>       
			<a href="#" data-role="button" data-rel="back" data-theme="b">取消</a>        
		</div>
		<script>
			function logout(){
				window.location.href ='<%=request.getContextPath()%>/userController/Logout/mobile';
			}
		</script>
	</div>

</body>
</html>