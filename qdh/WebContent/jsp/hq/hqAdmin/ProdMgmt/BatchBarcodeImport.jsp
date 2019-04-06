<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>朴与素连锁店管理信息系统</title>
<%@ include file="../../../common/Style.jsp"%>

<script>
$(document).ready(function(){
	parent.$.messager.progress('close'); 
});
function save(){

	if ($("#year_ID").combobox("getValue")== "")
		$.messager.alert('操作失败', "年份不能为空","error");
	else if ($("#quarter_ID").combobox("getValue") == "")
		$.messager.alert('操作失败', "季度不能为空","error");
	else if ($("#brand_ID").combobox("getValue") == "" || !isValidPositiveInteger($("#brand_ID").combobox("getValue")))
		$.messager.alert('操作失败', "品牌不能为空","error");
	else if ($("#uploadFile").val() == "")
		$.messager.alert('操作失败', "批量增加条码文件不能为空","error");
	else {
		$.messager.progress({
			title : '提示',
			text : '数据处理中，请稍后....'
		});
    	document.barcodeForm.action="<%=request.getContextPath()%>/prodOptController/HQBatchBarcodeImport";
    	document.barcodeForm.submit();
	}
}
</script>
</head>
<body>

    <form action="/action/productJSPAction!batchInsertBarcode" method="POST" name="barcodeForm" id="barcodeForm" enctype="multipart/form-data" theme="simple">

    <table width="85%" align="center"  class="OuterTable">
	    <tr><td>
			 <table width="100%" align="left" border="0">
			 	       <tr class="PBAOuterTableTitale" align="left">
	          				<th colspan="4">批量导入条码信息 <br/>
	          				- 请按照格式导入,任何一个错误条码都将导致所有新增条码失败
	          				</th>
	       				</tr>
	       					<tr class="InnerTableContent">
					         <td width="10%" height="40">年份</td>
					         <td width="30%"><form:select path="productBarcode.product.year.year_ID" cssClass="easyui-combobox"  style="width:80px;" data-options="editable:false"  size="1" id="year_ID" items="${yearList}"  itemValue="year_ID" itemLabel="year" /></td>
					         <td width="10%">季度</td>
					         <td width="50%"><form:select path="productBarcode.product.quarter.quarter_ID" cssClass="easyui-combobox" style="width:80px;" data-options="editable:false"  size="1" id="quarter_ID" items="${quarterList}"  itemValue="quarter_ID" itemLabel="quarter_Name"/>	</td>
				           </tr>
				           <tr class="InnerTableContent">
				           	 <td height="40">品牌</td>
					         <td><form:select path="productBarcode.product.brand.brand_ID" cssClass="easyui-combobox" style="width:120px;" size="1" id="brand_ID" items="${brandList}"  itemValue="brand_ID" itemLabel="brand_Name"/></td>
					         <td>条码文件</td>
					         <td><input type="file" name="uploadFile" id="uploadFile"/></td>
				           </tr>
						   <tr class="InnerTableContent">
						    <td height="25" align='left'>&nbsp;</td>
						    <td align='left' colspan="3">
						      <input type="button" value="上传导入条码" onclick="save();" />		
						    </td>
					      </tr>
			</table>
			<div class="errorAndmes">${msg}</div>
	   </td></tr>
	 </table>
	 </form>
</body>
</html>