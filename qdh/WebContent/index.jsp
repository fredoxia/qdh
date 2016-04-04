<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">
<%@ include file="jsp/common/JQMStyle.jsp"%>
<script>
$(document).bind('mobileinit',function(){
	$.mobile.loadPage.defaults.reloadPage = false;
});
function login() {
	if (validateLoginForm()){
		var params=$("#loginform").serialize();

		$.post('<%=request.getContextPath()%>/userController/login/mobile', params, 
		function(result) {
			if (result.success) {
				$.mobile.changePage('<%=request.getContextPath()%>/userController/Main/mobile', { 
				    transition: "slideup",
				    type:"post",
				    dataUrl:"<%=request.getContextPath()%>/userController/Main/mobile"
				});
			} else {
				renderPopup("登录错误",result.msg)
			}
		}, 'JSON');
	}
}

function validateLoginForm(){
	var userName = $("#id").val();
	var password = $("#password").val();
	if (userName == "" || password ==""){
		renderPopup("验证错误","登录名和密码不能为空");
		return false;
	}
	return true;
}
</script>
</head>
<body>
	<div data-role="page">

		<header data-role="header" data-theme="b">
			<h1>千禧在线订货</h1>
		</header>

		<div data-role="content" class="content">

			<p style="">
				登录系统
			</p>
			<form method="post" id="loginform">
				<div data-role="fieldcontainer">
					<label for="userName">用户名 : </label> <input type="number"
						id="id" name="id" placeholder="我们提供给你的数字登录账号" />
				</div>
				<div data-role="fieldcontainer">
					<label for="password">密码 : </label> <input type="number"
						id="password" name="password" required placeholder="四位数的数字密码" />
				</div>
				<div data-role="fieldcontainer">
					<input type="button" id="submitBt" data-theme="b" onclick ="login();" value="登录"/>
				</div>
			</form>
		</div>

		<footer data-role="footer" data-theme="a">
			<h1>©2016 千禧宝贝科技</h1>
		</footer>

		<jsp:include  page="jsp/common/Popup.jsp"/>

	</div>

</body>
</html>