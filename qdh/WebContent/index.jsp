<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="jsp/common/Style.jsp"%>
<title>千禧宝贝订货信息系统</title>
<script type="text/javascript" charset="utf-8">
function login(){
    var params=$("#loginForm").serialize(); 
    if (validateLoginForm()){
		$.post('<%=request.getContextPath()%>/userController/HQLogin', params,loginBackProcess,'json');
    }
}
function loginBackProcess(data){
	alert(data.success);
}
</script>
</head>
<body>
<div id="loginDialog" class="easyui-dialog" title="千禧宝贝连锁店管理系统" data-options="iconCls:'icon-status_online',resizable:false,modal:true,draggable:false,closable:false,buttons:[{text:'登陆',handler:function(){ login(); }}]" style="width:330px;height:200px;padding:5px">
	  <form id="loginForm" name="loginForm" method="post" action="">
		  <table width="100%">
		    <tr>
		      <td colspan="2" align="center"><strong>总部用户登录 </strong></td>
		    </tr>
		    <tr>
		      <td width="85" height="30">用户名：</td>
		      <td width="180">
		      <input type="text" name="userName" id="userName" class="easyui-validatebox" data-options="required:true"/></td>
		    </tr>
		    <tr>
		      <td height="30">密码：</td>
		      <td>
		      <input type="password" name="password" id="password" class="easyui-validatebox" data-options="required:true"/>

		      </td>
		    </tr>
		  </table>
		</form>
</div>


</body>
</html>