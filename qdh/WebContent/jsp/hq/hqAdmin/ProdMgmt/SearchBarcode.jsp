<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%> 
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>查询条形码</title>
<%@ include file="../../../common/Style.jsp"%>

<script>
$(document).ready(function(){
	parent.$.messager.progress('close'); 
});

function exportBarcodeToExcel(){
	alert("功能还未生成");
	return ;
	if (validateCheckbox()){
		var url = "action/productJSPAction.action";
		document.searchedBarcodeForm.action = url;
		document.searchedBarcodeForm.submit();	
	}
}



function searchBarcode(){
    $.messager.progress({
			text : '数据获取中，请稍后....'
		});
   var params=$("#barcodeSearchForm").serialize();  
   $.post("<%=request.getContextPath()%>/prodOptController/SearchBarcode",params, backProcess,"json");
}

function backProcess(data){
	$.messager.progress('close'); 
	clearAllData();
	
	var barcodes = data.obj;
	
    $('#orgTablebody tr').each(function () {                
        $(this).remove();
    });
    console.log(JSON.stringify(data)) ;
    if (barcodes.length != 0){
	    for (var i = 0; i < barcodes.length; i++){
	    	var j = i+1;
	    	var bg = "";
	    	if ((i % 2) == 0)
	    		bg = " style='background-color: rgb(255, 250, 208);'";
	        if (barcodes[i] != "")  {
		          $("<tr align='center' class='InnerTableContent'" + bg +"><td><input type='checkbox' name='selectedBarcodes' value='"+barcodes[i].barcode+"'/></td><td>"+
				          j+"</td><td>"+
				          barcodes[i].product.year.year + " " + barcodes[i].product.quarter.quarter_Name +"</td><td>"+
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
				          barcodes[i].barcode+"</td><td><a href='#' onclick=\"window.open ('<%=request.getContextPath()%>/prodOptController/HQSearchForUpdate?barcode="+barcodes[i].barcode+"','新窗口','height=550, width=400, toolbar=no, menubar=no, scrollbars=no, resizable=no, location=no, status=no');\"><img src='<%=request.getContextPath()%>/conf_files/web-image/editor.gif' border='0'/></a></td><td></td></tr>").appendTo("#orgTablebody");
	        }
	    }

	    $("<tr class='InnerTableContent'><td colspan=14><div id='error' style='color:red;font-size:13px'></div><div id='tip'></div></td></tr>").appendTo("#orgTablebody");
        $("<tr class='InnerTableContent' style='background-color: rgb(255, 250, 208);' align='left'><td></td><td colspan=13><input type='button' value='导出产品信息' onclick='exportBarcodeToExcel();'/></td></tr>").appendTo("#orgTablebody");
    }else {
    	$("<tr class='InnerTableContent' style='background-color: rgb(255, 250, 208);' align='center'><td colspan=14><font color='red'>对应信息没有查询信息</font> </td></tr>").appendTo("#orgTablebody");
    }  
}


function clearAllData(){
	$("#error").html("");
	$("#tip").html("");

    $('#orgTablebody tr').each(function () {                
        $(this).remove();
    });
}
function showCreateDate(){
	if ($("#needCreateDate").attr("checked") == 'checked')
		$("#createDateDiv").show();
	else
		$("#createDateDiv").hide();
}
function selectAll(){

	if ($("#selectAllCheck").prop("checked") == true)
		$("input[name='selectedBarcodes']").prop("checked",true); 
	else
		$("input[name='selectedBarcodes']").prop("checked",false); 
	
}


</script>


</head>
<body>
 	<div class="easyui-layout"  data-options="fit : true,border : false">
		<div data-options="region:'north',border:false" style="height: 170px;">
			<form id="barcodeSearchForm" action="" method="POST" theme="simple">
			 <table width="100%" border="0">
			    <tr class="InnerTableContent">
			      <td width="80" height="19"><strong>年份：</strong></td>
			      <td width="90"><form:select path="productBarcode.product.year.year_ID" cssClass="easyui-combobox"  style="width:80px;" data-options="editable:false"  size="1" id="year_ID" >
			                           <form:option value="-99" label="- 全选  -"/>  
                                       <form:options items="${yearList}"  itemValue="year_ID" itemLabel="year"/> 
			                     </form:select>
			      </td>
			      <td width="79"><strong>季度：</strong></td>
			      <td width="90"> <form:select path="productBarcode.product.quarter.quarter_ID" cssClass="easyui-combobox" style="width:80px;" data-options="editable:false"  size="1" id="quarter_ID">
			                           <form:option value="-99" label="- 全选  -"/>  
                                       <form:options  items="${quarterList}"  itemValue="quarter_ID" itemLabel="quarter_Name"/> 
			                     </form:select>			      
			      </td>
			      <td width="59"><strong>品牌：</strong></td>
			      <td width="357"><form:select path="productBarcode.product.brand.brand_ID" cssClass="easyui-combobox" style="width:120px;" size="1" id="brand_ID" > 
			      			           <form:option value="-99" label="- 全选  -"/>  
                                       <form:options items="${brandList}"  itemValue="brand_ID" itemLabel="brand_Name"/> 
			                     </form:select>	
			      
			      </td>
			    </tr>
			    <tr class="InnerTableContent">
			      <td height="19"><strong>货号：</strong></td>
			      <td><form:input path="productBarcode.product.productCode"  cssClass="easyui-textbox" style="width:80px;" id="productCode" /></td>
			      <td><strong>货品类：</strong></td>
			      <td><form:select path="productBarcode.product.category.category_ID" cssClass="easyui-combobox" style="width:120px;"  id="category_ID" >
			      			           <form:option value="-99" label="- 全选  -"/>  
                                       <form:options items="${categoryList}" itemValue="category_ID" itemLabel="category_Name"/> 
			           </form:select>				      
			      
			      </td>
			      <td>&nbsp;</td>
		        </tr>
			    <tr class="InnerTableContent">
			      <td height="19"><strong>录入时间：</strong></td>
			      <td height="19" colspan="3">
			        <table border="0">
			           <tr>
			               <td></td>
			               <td>
						                      开始<form:input id="startDate" path="formBean.startDate" cssClass="easyui-datebox"  data-options="width:100,editable:false"/>
									&nbsp;&nbsp;&nbsp;
							             截止 <form:input id="endDate" path="formBean.endDate" cssClass="easyui-datebox"  data-options="width:100,editable:false"/>
						   </td>
			           </tr>
			        </table>			       </td>
			      <td height="19">&nbsp;</td>
		        </tr>
			    <tr class="InnerTableContent">
			      <td height="30">&nbsp;</td>
			      <td><input type="button" name="saveButton" value="查询条形码 " onClick="searchBarcode();" /> </td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			      <td>&nbsp;</td>
			    </tr>
			    </table>
			</form>
		</div>
		<div data-options="region:'center',border:false">
			<jsp:include page="ProductListTableCheckbox.jsp"/>
		</div>
	</div>
<br/>
</body>
</html>