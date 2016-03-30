<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">
<%@ include file="jsp/common/JQMStyle.jsp"%>
<script>
function login() {
	var params=$("#loginform").serialize();
	alert(params);
	$.post('<%=request.getContextPath()%>/userController/login/mobile', params, 
	function(result) {
		if (result.success) {
			$.messager.alert('信息', result.msg, 'info');
		} else {
			$("#loginFail").popup("open");
		}
	}, 'JSON');
}
</script>
</head>
<body>
	<section id="page1" data-role="page">

		<header data-role="header" data-theme="b">
			<h1>千禧宝贝在线订货系统</h1>
		</header>

		<div data-role="content" class="content">

			<p style="">
				登录系统
			</p>
			<form method="post" id="loginform">
				<div data-role="fieldcontainer">
					<label for="userName">用户名 : </label> <input type="number"
						id="userName" name="userName" placeholder="我们提供给你的数字登录账号" />
				</div>
				<div data-role="fieldcontainer">
					<label for="password">密码 : </label> <input type="number"
						id="password" name="password" placeholder="四位数的数字密码" />
				</div>
				<div data-role="fieldcontainer">
					<a data-role="button" id="submit" data-theme="b" onclick ="login();">登录</a>
				</div>
			</form>
		</div>

		<footer data-role="footer">
			<h1>©2016 千禧宝贝科技</h1>
		</footer>
		<div id="loginFail" data-role="popup">登陆失败</div>
	</section>
</body>
</html>