<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>产品条形码导入</title>
<%@ include file="../../../common/Style.jsp"%>
<script>
$(document).ready(function(){
	parent.$.messager.progress('close'); 
});
function checkProductCodeSerialNum(){
    var params=$("#barcodeGenForm").serialize(); 
    $.post("<%=request.getContextPath()%>/prodOptController/CheckProductCodeSerialNum",params, checkProductCodeBackProcess,"json");	
}
function checkProductCodeBackProcess(data){
	var returnCode = data.returnCode;
	var tips = data.msg;

	if (returnCode == FAIL){
		$.messager.alert('操作失败', tips,'error');
    } else if (returnCode == WARNING){
		tips += "\n 你确定是否继续生成货品和条码?";
		var con = confirm(tips);
		if(con == true){
			generateBarcode();
		}
    } else if (returnCode == SUCCESS){
    	generateBarcode();
    }

	$("#saveButton").linkbutton("enable");
}
function generateBarcode(){
	$("#color").find("option").attr("selected","selected"); 
    var params=$("#barcodeGenForm").serialize(); 

    $.post("<%=request.getContextPath()%>/prodOptController/GenerateProductBarcode",params, saveBarcodeBackProcess,"json");
}

function saveProduct(){
    $("#saveButton").linkbutton("disable");
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
	
		if (error == ""){
			var colors = $("#color").text();
	
	        if (colors !="" ){
	        	var msg ="你确定要生成此颜色组和尺码组条码:\n" + colors;
			    if (confirm(msg))
			       checkProductCodeSerialNum();
				else 
					$("#saveButton").linkbutton("enable");
	        } else 
	        	checkProductCodeSerialNum();
		} else{
			$.messager.alert('操作失败', error,'error');
			$("#saveButton").linkbutton("enable");
		}
    } else 
    	$("#saveButton").linkbutton("enable");
}

function saveBarcodeBackProcess(data){
	var returnCode = data.returnCode;

	if (returnCode != SUCCESS){
		$.messager.alert('操作失败', data.msg,'error');
	}else {
		clearAllData();
	
	    var barcodes = data.obj;

	    if (barcodes.length != 0){
	    	
		    for (var i = 0; i < barcodes.length; i++){

		    	var bg = "";
		    	if ((i % 2) == 0)
		    		bg = " style='background-color: rgb(255, 250, 208);'";
		        if (barcodes[i] != "" && barcodes[i].product != undefined)  {

			          $("<tr align='center' class='InnerTableContent'" + bg +"><td>"+barcodes[i].product.year.year + " " + barcodes[i].product.quarter.quarter_Name +"</td><td>"+
					          barcodes[i].product.brand.brand_Name+"</td><td>"+
					          barcodes[i].product.category.category_Name+"</td><td>"+
					          barcodes[i].product.productCode+"</td><td>"+
					          parseColorValue(barcodes[i].color)+"</td><td>"+
					          barcodes[i].product.numPerHand + "/" + barcodes[i].product.unit +"</td><td>"+
					          barcodes[i].product.salesPriceFactory +"</td><td>"+
					          barcodes[i].product.discount +"</td><td>"+
					          barcodes[i].product.salesPrice+"</td><td>"+
					          barcodes[i].product.recCost+"</td><td>"+
					          barcodes[i].product.wholeSalePrice+"</td><td>"+
					          barcodes[i].barcode+"</td><td>"+
					          barcodes[i].createDate+"</td><td></td><td></td></tr>").appendTo("#orgTablebody");
		         } 
		    }
		    
	        alert("成功生成条码");
	    }else {
	    	$("<tr class='InnerTableContent' style='background-color: rgb(255, 250, 208);' align='center'><td colspan=15><font color='red'>对应信息没有条形码存在</font> </td></tr>").appendTo("#orgTablebody");
	    }
    }

	$("#saveButton").attr("disabled", false);
}



function getProductColors(){
	var serialNum = $("#serialNum").textbox("getValue");
	var params = "serialNum=" + serialNum;
    $.post("<%=request.getContextPath()%>/prodOptController/GetProductInforBySerialNum",params, getProductBySNBackProcess,"json");
}

function getProductBySNBackProcess(data){
	
	var response = data;
	var returnCode = response.returnCode;
	if (returnCode != SUCCESS)
		alert(response.message);
	else {
		var returnValue = response.obj;
		var colors = returnValue.color;
		$("#colorsDiv").html(colors);

		var productInfor = returnValue.product;
		assignProductValue(productInfor);
	}
}
function assignProductValue(p){
	
	if (p != undefined && p != null){
		$("#salesPrice").textbox("setValue",p.salesPrice);
		$("#productCode").textbox("setValue",p.productCode);
		$("#recCost").textbox("setValue",p.recCost);
		$("#wholeSalePrice").textbox("setValue",p.wholeSalePrice);
		$("#discount").textbox("setValue",p.discount);
		$("#numPerHand").combobox("setValue",p.numPerHand);
		$("#unit").combobox("setValue",p.unit);
		$("#year_ID").combobox("setValue",p.year.year_ID);
		$("#quarter_ID").combobox("setValue",p.quarter.quarter_ID);
		$("#brand_ID").combobox("setValue",p.brand.brand_ID);
		$("#salesPrice").textbox("setValue",p.salesPrice);

		$("#category_ID").combobox("setValue",p.category.category_ID);
		
	}
}
function clearAllData(){
	$("#error").html("");

	$("#salesPrice").attr("value","");
	$("#barcode").attr("value","");
	$("#productCode").attr("value","");
	$("#recCost").attr("value","");
	$("#wholeSalePrice").attr("value","");
	$("#wholeSalePrice2").attr("value","");
	$("#wholeSalePrice3").attr("value","");
	$("#salesPriceFactory").attr("value","");
	$("#serialNum").attr("value","");
	$("#color").empty();
	$("#colorName").attr("value","");
	
    $('#orgTablebody tr').each(function () {                
        $(this).remove();
    });
}
/**
 * once click the button, it will help to search brand
 */
function searchColor(){
	var colorName = $.trim($("#colorName").val());
	if (colorName != "") {
	    var params= "colors=" + colorName  ; 
    
        var url = encodeURI(encodeURI("<%=request.getContextPath()%>/prodOptController/SearchColor?" +params));
	
        window.open(url,'新窗口','height=400, width=300, toolbar=no, menubar=no, scrollbars=yes, resizable=yes, location=no, status=no');  
	} else {
        alert("请输入颜色名称");
    }; 
}
function selectColor(data){
	var dataArray = data.split(";");
	$.each(dataArray,function(key,val){
	    if (val != ""){
		    var added = false;
            var colorArray = val.split(",");
            $("#color option").each(function(){
            	   if($(this).val() == colorArray[0])
            	      added = true;
            	   });
     	   if (added == false)
              $("#color").append("<option value='"+ colorArray[0]+"'>"+ colorArray[1]+"</option>");
		}
	});
}
function removeColor(){
	$("#color").find("option:selected").each(function(){
	   var removeColor = $(this).val();
	   if (removeColor != 0 && removeColor != undefined)
		   $("#color option[value='"+removeColor+"']").remove();  
       });
}
function clickSize(){
	var sizes = $("#size").val().toString().split(",");
	if (sizes[0] == "")
		$("#size").find("option:selected").attr("selected", false);
}

</script>

</head>
<body>
<form id="barcodeGenForm" class="easyui-form"  action="generateBarcode" method="POST">
 <table width="100%" border="0" >   
    <tr class="InnerTableContent">
      <td height="19" ><strong>年份：</strong></td>
      <td>
      		<form:select path="productBarcode.product.year.year_ID" cssClass="easyui-combobox"  style="width:80px;" data-options="editable:false"  size="1" id="year_ID" items="${yearList}"  itemValue="year_ID" itemLabel="year" />     
      </td>
      <td><strong>季度：</strong></td>
      <td>
            <form:select path="productBarcode.product.quarter.quarter_ID" cssClass="easyui-combobox" style="width:80px;" data-options="editable:false"  size="1" id="quarter_ID" items="${quarterList}"  itemValue="quarter_ID" itemLabel="quarter_Name"/>
      </td>
      <td><strong>品牌：</strong></td>
      <td colspan="2">
          <form:select path="productBarcode.product.brand.brand_ID" cssClass="easyui-combobox" style="width:120px;" size="1" id="brand_ID" items="${brandList}"  itemValue="brand_ID" itemLabel="brand_Name"/> 
      </td>
      
      <td></td>    
      <td></td> 
      <td></td> 
      <td></td> 
      <td>&nbsp;</td>     
    </tr>
    <tr class="InnerTableContent">
      <td height="19"><strong>齐码数量：</strong></td>
      <td>
          <form:select path="productBarcode.product.numPerHand" cssClass="easyui-combobox" style="width:80px;" data-options="editable:false" size="1" id="numPerHand" items="${numPerHandList}" itemValue="numPerHand" itemLabel="numPerHand"/>     
      </td>
      <td><strong>货品类：</strong></td>
      <td>
          <form:select path="productBarcode.product.category.category_ID" cssClass="easyui-combobox" style="width:120px;"  id="category_ID" items="${categoryList}" itemValue="category_ID" itemLabel="category_Name" />      </td>
      <td><strong>单位：</strong></td>
      <td>
      	 <form:select path="productBarcode.product.unit"  cssClass="easyui-combobox" style="width:80px;" data-options="editable:false" size="1" id="unit" items="${unitList}" itemValue="productUnit" itemLabel="productUnit"/>
      </td>
      <td>&nbsp;</td>
      <td></td>  
      <td></td> 
      <td></td> 
      <td></td> 
      <td>&nbsp;</td>        
    </tr>
   <tr class="InnerTableContent">
      <td height="4" colspan="12"><hr width="100%" color="#FFCC00"/></td>
    </tr>
    <tr class="InnerTableContent">
		      <td width="100" height="19"><strong>货号：</strong></td>
		      <td width="110">
		       <form:input path="productBarcode.product.productCode"  cssClass="easyui-textbox" style="width:80px;" id="productCode"   data-options="required:true,validType:['required','length[3,20]']" />*
		      </td>
		      <td width="80"><strong>进价:</strong></td>
		      <td width="110">  
		        <form:input path="productBarcode.product.recCost" cssClass="easyui-numberbox" style="width:80px;" id="recCost" size="9" data-options="required:true,min:0,max:999,precision:0" />
		      </td>
		      <td width="100"><strong>厂家零售价：</strong></td>
		      <td width="110"><form:input path="productBarcode.product.salesPriceFactory" cssClass="easyui-numberbox" style="width:80px;" id="salesPriceFactory" size="9" data-options="min:0,max:999,precision:0"/></td>
		      <td width="100"></td>
		      <td width="80"></td>
		      <td width="100"></td> 
      		  <td width="80">&nbsp;</td> 
      		  <td width="100">&nbsp;</td> 
     		  <td>&nbsp;</td>  
		    </tr>
		    <tr class="InnerTableContent">
              <td><strong>连锁零售价：</strong></td>
		      <td><form:input path="productBarcode.product.salesPrice" id="salesPrice"  cssClass="easyui-numberbox" style="width:80px;" size="9" data-options="required:true,min:0,max:999,precision:0"/></td>
		      <td height="19"><strong>预设价：</strong></td>
		      <td><form:input path="productBarcode.product.wholeSalePrice" id="wholeSalePrice"  cssClass="easyui-numberbox" style="width:80px;" size="9" data-options="required:true,min:0,max:999,precision:0"/></td>
		      <td><strong>折扣:</strong></td>
		      <td><form:input path="productBarcode.product.discount" id="discount"  cssClass="easyui-numberbox" style="width:80px;" size="9" data-options="min:0,max:1,precision:2"/></td>
		      <td></td>
		      <td></td>
		      <td></td> 
      		  <td>&nbsp;</td> 
      		  <td>&nbsp;</td> 
     		  <td>&nbsp;</td> 		      
		    </tr>  
    <tr class="InnerTableContent">
		  <td height="4" colspan="12"><hr width="100%" color="#FFCC00"/></td>
	</tr>
    <tr class="InnerTableContent">
      <td height="19"><strong>商品编码：</strong></td>
      <td colspan="11"><form:input path="productBarcode.product.serialNum" id="serialNum"  cssClass="easyui-textbox" style="width:80px;"/> 
      <a href="#" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="getProductColors();">获取已录信息</a>(*当需要为已经存在的货品添加额外颜色和尺码的条码时，请输入)<br/> <div id="colorsDiv"></div></td>
    </tr>
    <tr class="InnerTableContent">
      <td height="4" colspan="12"><hr width="100%" color="#FFCC00"/></td>
    </tr>
    <tr class="InnerTableContent">
      <td height="19"><strong>颜色：</strong></td>
      <td colspan="8">*多个颜色间用 - 分开,比如"红-黑-黄"<br/>
          <input type="text" id="colorName" size="10" onfocus="this.select();"/><input id="searchColorBt" type="button" onclick="searchColor();" value="查找"/><br/><br/>
          <select name="colorIds" id="color" multiple size="5" style="width:94px"></select><input id="searchColorBt" type="button" onclick="removeColor();" value="删除"/></td>
      <td></td>
      <td><!--<strong>尺码：</strong>--></td>
      <td><!--<s:select name="formBean.sizeIds" id="size" list="uiBean.basicData.sizeList" listKey="sizeId" listValue="name"  headerKey="" headerValue="--------无尺码---------"  multiple="true" size="15" onclick="clickSize();"/>--></td>
    </tr>

    <tr class="InnerTableContent">
      <td height="4" colspan="12"><hr width="100%" color="#FFCC00"/></td>
    </tr>
    <tr class="InnerTableContent">
      <td height="30">&nbsp;</td>
      <td colspan="3"><a href="#" id="saveButton" class="easyui-linkbutton" data-options="iconCls:'icon-search'" onclick="saveProduct();">保存产品信息 </a>
      </td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td>
      <td>&nbsp;</td> 
      <td>&nbsp;</td> 
      <td>&nbsp;</td> 
      <td>&nbsp;</td>       
    </tr>
  </table>

</form>
<jsp:include page="ProductListTable.jsp"/>

<br/>
</body>
</html>