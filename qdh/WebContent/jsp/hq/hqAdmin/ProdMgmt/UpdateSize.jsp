<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">

<title>朴与素连锁店管理信息系统</title>
<script>
function validateForm(){
	var size = $("#size").val();

	var error = "";
	if (size == "" || size.length > 4){
       error = error + "尺码长度必须是大于0小于4\n";
	}

	if (error != ""){
	       alert(error);
	       return false;
	}
	return true;	
}
</script>
</head>
<body>
    <%@ include file="../../../common/Style.jsp"%>
    <table width="90%" align="center"  class="OuterTable">
    <tr><td>
        <s:form id="updateSizeForm" name="updateSizeForm" method="post" action="action/basicData!saveUpdateSize" theme="simple" onsubmit="return validateForm();">
	    <table width="100%" border="0">
	       <tr class="PBAOuterTableTitale">
	          <td colspan="2">新建/更新尺码信息</td>
	       </tr>
	       <tr>
	          <td colspan="2"><font color="red"><s:fielderror/></font><s:actionmessage/>
	          </td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td>尺码序列号:</td><td><s:textfield name="formBean.size.sizeId" readonly="true"/></td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td>尺码    :</td><td><s:textfield id="size" name="formBean.size.size"/>*</td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td colspan="2"><input type="submit" value="新建/修改"/><input type="button" value="取消" onclick="window.close();"/></td>
	       </tr>
	    </table>
	    </s:form>
	    </td>
	</tr>
	</table>


	      
</body>
</html>