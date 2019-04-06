<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>修改条型码资料</title>
<%@ include file="../../../common/Style.jsp"%>
<script>
$(document).ready(function(){

});

function del(){
	var info = "你确定删除此商品条码信息?\n此商品信息一经删除所有相关单据都将删除对应信息!";
	$.messager.confirm('确认操作', info, function(r){
		if (r){
			var params=$("#barcodeGenForm").serialize(); 

		    $.post("<%=request.getContextPath()%>/prodOptController/DeleteProduct",params, deleteBarcodeBackProcess,"json");
		}
	});

}
function deleteBarcodeBackProcess(data){
	var returnCode = data.returnCode;

	if (returnCode != SUCCESS){
		$.messager.alert('操作失败', data.msg,'error');
	}else {	
        $.messager.alert({
    		title:'操作成功',
    		msg:'商品已经删除',
    		fn: function(){
    			window.close();
    		}
    	});
	
	}
}
function update(){
	  if ($('#barcodeGenForm').form('validate')){
			var error = "";
			if ($("#year_ID").combobox("getValue") == ""){
		          error += "年份 - 是必选项\n";
			} 
			if ($("#quarter_ID").combobox("getValue") == ""){
		          error += "季度 - 是必选项\n";
			} 
			var brandId = $("#brand_ID").combobox("getValue");
			if (brandId == "" || brandId == 0){
		          error += "品牌 - 是必选项\n";
			} else if (!isValidPositiveInteger(brandId)) {
		        error += "品牌 - 必须是系统已经存在的信息，请检查\n";
			}
			
			var categoryId = $("#category_ID").combobox("getValue");
			if (categoryId == ""){
		          error += "货品类 - 是必选项\n";
			} else if (!isValidPositiveInteger(categoryId)) {
		        error += "货品类 - 必须是系统已经存在的类别，请检查\n";
			}
		
			
			if ($("#productCode").textbox("getValue") == ""){
		          error += "货号 - 是必选项\n";
			}
			
			if (error != "")
				alert(error);
			else {
			    var params=$("#barcodeGenForm").serialize(); 

			    $.post("<%=request.getContextPath()%>/prodOptController/UpdateProduct",params, saveBarcodeBackProcess,"json");
			}
	  }
}

function saveBarcodeBackProcess(data){
	var returnCode = data.returnCode;

	if (returnCode != SUCCESS){
		$.messager.alert('操作失败', data.msg,'error');
	}else {	
        $.messager.alert({
    		title:'操作成功',
    		msg:'商品更新成功',
    		fn: function(){
    			window.close();
    		}
    	});
	
	}
}
</script>
</head>
<body>
    <table width="90%" align="center"  class="OuterTable">
    <tr><td>
        <form id="barcodeGenForm" name="barcodeGenForm"  class="easyui-form"  method="post" action="" theme="simple">
	    <table width="100%" border="0" id="org_table">
	       <tr class="PBAOuterTableTitale">
	          <th colspan="2">修改条形码信息</th>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>商品编码</strong>    :</td>
	          <td>${productBarcode.product.serialNum}
	          <form:hidden path="productBarcode.id"/>
	          <form:hidden path="productBarcode.product.productId"/></td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td height="18"><strong>条形码 </strong>  : </td>
	          <td>${productBarcode.barcode}
	          </td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>品牌 </strong>        :</td>
	          <td><form:select path="productBarcode.product.brand.brand_ID" cssClass="easyui-combobox" style="width:120px;" size="1" id="brand_ID" items="${brandList}"  itemValue="brand_ID" itemLabel="brand_Name"/></td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>年份  </strong>       :</td>
	          <td><form:select path="productBarcode.product.year.year_ID" cssClass="easyui-combobox"  style="width:80px;" data-options="editable:false"  size="1" id="year_ID" items="${yearList}"  itemValue="year_ID" itemLabel="year" /></td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td height="18"><strong>季度</strong>         :</td>
	          <td><form:select path="productBarcode.product.quarter.quarter_ID" cssClass="easyui-combobox" style="width:80px;" data-options="editable:false"  size="1" id="quarter_ID" items="${quarterList}"  itemValue="quarter_ID" itemLabel="quarter_Name"/></td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>类别</strong>       :</td>
	          <td><form:select path="productBarcode.product.category.category_ID" cssClass="easyui-combobox" style="width:120px;"  id="category_ID" items="${categoryList}" itemValue="category_ID" itemLabel="category_Name" /></td>
	       </tr>

	      <tr class="InnerTableContent">
	          <td height="18"><strong>产品货号</strong>:</td><td><form:input path="productBarcode.product.productCode"  cssClass="easyui-textbox" style="width:80px;" id="productCode"   data-options="required:true,validType:['required','length[3,20]']" /></td>
	       </tr>	
	       <tr class="InnerTableContent">
	          <td height="18"><strong>齐码数量 </strong>   :</td>
	          <td><form:select path="productBarcode.product.numPerHand" cssClass="easyui-combobox" style="width:80px;" data-options="editable:false" size="1" id="numPerHand" items="${numPerHandList}" itemValue="numPerHand" itemLabel="numPerHand"/>  </td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td height="18"><strong>单位 </strong> :</td>
	          <td><form:select path="productBarcode.product.unit"  cssClass="easyui-combobox" style="width:80px;" data-options="editable:false" size="1" id="unit" items="${unitList}" itemValue="productUnit" itemLabel="productUnit"/></td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>颜色</strong>         :</td><td>${productBarcode.color.name}</td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>连锁零售价 </strong>       :</td><td><form:input path="productBarcode.product.salesPrice" id="salesPrice"  cssClass="easyui-numberbox" data-options="min:0,max:999,precision:0"/></td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td height="18"><strong>进价 </strong>       :</td><td><form:input path="productBarcode.product.recCost" id="recCost"  cssClass="easyui-numberbox" data-options="min:0,max:999,precision:0"/></td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>预设价1 </strong>       :</td><td><form:input path="productBarcode.product.wholeSalePrice" id="wholeSalePrice"  cssClass="easyui-numberbox" data-options="min:0,max:999,precision:0"/></td>
	       </tr>
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td height="18"><strong>厂家零售价 </strong>       :</td><td><form:input path="productBarcode.product.salesPriceFactory" id="salesPriceFactory"  cssClass="easyui-numberbox" data-options="min:0,max:999,precision:0"/></td>
	       </tr>
	       <tr class="InnerTableContent">
	          <td height="18"><strong>折扣 </strong>       :</td><td><form:input path="productBarcode.product.discount" id="discount"  cssClass="easyui-numberbox" data-options="min:0,max:1,precision:2"/></td>
	       </tr>	
	                
	       <tr class="InnerTableContent" style="background-color: rgb(255, 250, 208);">
	          <td colspan="2"> <input type="button" value="更新" onclick="update();"/>&nbsp;&nbsp;<input type="button" value="删除" onclick="del();"/>&nbsp;&nbsp;<input type="button" value="取消" onclick="window.close();"/></td>
	       </tr>
	    </table>
	    </form>
	    </td>
	</tr>
	</table>   
</body>
</html>