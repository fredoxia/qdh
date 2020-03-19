<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>朴与素连锁店管理信息系统</title>
<script>
function selectColor(){
	var str = "";
	if ($("input[name='selectColor']:checked").length == 0){
		alert("请先选中颜色");
	} else {
		$("input[name='selectColor']:checked").each(function(){  
			str+=$(this).val()+";";  
		});
		window.opener.selectColor(str);
		window.close();
	}
}
</script>
</head>
<body>
   <%@ include file="../../../common/Style.jsp"%>
   <table width="100%"  align="left" class="OuterTable">
		  <tr class="PBAInnerTableTitale" align='left'>
		    <th width="35" height="35"></th>
		    <th width="110">颜色</th>
		  </tr>
	      <c:forEach items="${colors}" var="color">
			    <tr class="InnerTableContent" >	      
			      <td height="25"><input type='checkbox' name='selectColor' value='${color.colorId},${color.name}'/></td>
			      <td>${color.name}</td>
			    </tr>
	       </c:forEach>
	       <c:if test="${empty colors}">
			 	<tr height="22" class="InnerTableContent" align="center">
			 	        <td colspan="2">没有找到相应结果</td>
			 	 </tr>
		   </c:if>
		   <tr class="PBAInnerTableTitale" align='left'>
		    <th height="35"><input type="button" value="选择" onclick ="selectColor();"/></th>
		    <th></th>
		  </tr> 
	</table>
	<br/>
</body>
</html>