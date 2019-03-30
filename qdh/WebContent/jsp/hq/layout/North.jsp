<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<div id="version" style="position: absolute; left: 0px; bottom: 2px;" class="alert alert-info">
		<img src='<%=request.getContextPath()%>/conf_files/web-image/qdh-logo.jpg' height='43' width='230' align="left">
    </div>
    <div id="version" style="position: absolute; left: 250px; bottom: 4px;" class="alert alert-info">
	     订货系统 V0.1
    </div>
    
	<div id="sessionInfoDiv" style="position: absolute; right: 3px; top: 3px;" class="alert alert-info">
	    欢迎 <c:out value="${sessionScope.HQ_SESSION_INFO.userName}" escapeXml="false" default="未登陆"></c:out>
    </div>
    <div style="position: absolute; right: 0px; bottom: 1px;">
        <!--  
	    <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_pfMenu',iconCls:'cog'">更换皮肤</a> 
	    <a href="javascript:void(0);" class="easyui-menubutton" data-options="menu:'#layout_north_kzmbMenu',iconCls:'cog'">控制面板</a>-->
	    <a href="<%=request.getContextPath()%>/userController/HQLogoff" class="easyui-linkbutton">退出系统</a>
    </div>
