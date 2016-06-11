<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta name="viewport" content ="width=device-width, initial-scale=1">

<%@ include file="../common/JQMStyle.jsp"%>
<script>
$(document).ready(function(){
	$("#productCode").focus();

})
function clearProductCode(){
	$("#productCode").focus();
	$("#productCode").attr("value","");
	
    $("#products").hide();
    
    $('#productBody tr').each(function () {                
        $(this).remove();
    });

}
function checkSearch(){

	if ($.trim($("#productCode").val()).length >= 3)
		searchProduct();
}
function searchProduct(){
	if (validateSearch()){
		var params = "productCode=" + $("#productCode").val();

		$.mobile.loading("show",{ theme: "b", text: "正在加载数据", textonly: false});
		
		$.post('<%=request.getContextPath()%>/orderController/SearchProduct/mobile', params, 
		function(result) {
			
			if (result.success) {
			    $('#productBody tr').each(function () {                
			        $(this).remove();
			    });
			    
			    var cops = result.obj;
			    if (cops != null && cops.length != 0){
				    for (var i = 0; i < cops.length; i++){
				    	
				    	var j = i +1;
				        if (cops[i] != "")  {
					          $("<tr id='pRow"+cops[i].pbId+"'><td style='vertical-align:middle;'>"+
					        		  cops[i].brand +"</td><td style='vertical-align:middle;'>"+
					        		  cops[i].productCode +" " + cops[i].color+"</td><td style='vertical-align:middle;' id='pQ"+cops[i].pbId+"'>"+
					        		  cops[i].quantity+"</td><td style='vertical-align:middle;'>"+
					        		  cops[i].retailPrice+"</td><td>"+
										"<div name='btnGroup' data-role='controlgroup' data-type='horizontal'>"+
											"<input name='addBtn' type='button' value='加订' data-mini='true'  data-inline='true' onclick='addOrder("+cops[i].pbId+");'/>"+
											"<input name='addBtn' type='button' value='减订' data-mini='true'  data-inline='true' onclick='deductOrder("+cops[i].pbId+");'/>"+
										"</div>"+
							          "</td></tr>").appendTo("#productBody");
				        }
				    }
			    } else {
			    	$("<tr><td colspan=5><font color='red'>没有查询到产品</font> </td></tr>").appendTo("#productBody");
			    }
			    
			    $("#products").show();
			    $("[name='addBtn']").button();
			    $("[name='btnGroup']").controlgroup();

			    $.mobile.loading("hide");
			} else {
				$.mobile.loading("hide");
				renderPopup("系统错误",result.msg)
			}
		}, 'JSON');
	}
}
function validateSearch(){

	if ($.trim($("#productCode").val()).length < 1){
		renderPopup("查询错误","请输入至少一位货号作为查询条件");
		$("#productCode").focus();
		return false;
	} else 
		return true;
}
function myOrder(pbId, quantity){
	$.mobile.loading("show",{ theme: "b", text: "正在加载数据", textonly: false});
	var params="pbId=" + pbId + "&quantity=" + quantity;

	$.post('<%=request.getContextPath()%>/orderController/StartOrderMore/mobile', params, 
	function(result) {
		$.mobile.loading("hide");
		if (result.success) {
			var resultData = result.obj;
			var pQ = resultData.pQ;
			var pSum = resultData.pSaleP;
			$("#pQ" + pbId).html(pQ);

		} else if (result.returnCode == WARNING){
			$("#pQ" + pbId).html(0);

		} else {
			renderPopup("系统错误",result.msg)
		}
	}, 'JSON');
}
function deductOrder(pbId){
	myOrder(pbId, -1);
}
function addOrder(pbId){
	myOrder(pbId, 1);
}
</script>
</head>
<body>
	<div id="mainPage" data-role="page">

		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div data-role="content" class="content">
				<table>
				    <tr>
						<td><label for="productCode">货号 : </label></td> 
						<td><input type="number" id="productCode" name="productCode"  placeholder="输入至少三位货号,自动查找" onkeyup="checkSearch();"/></td>
					</tr>

				</table>
				<div class="ui-grid-a ui-responsive">
    				<div class="ui-block-a"><input type="button" id="searchBnt" data-theme="a" onclick ="searchProduct();" value="查找货品"/></div>
    				<div class="ui-block-b"><input type="button" id="clearBnt" data-theme="b" onclick ="clearProductCode();" value="清空查询条件"/></div>
				</div>
				<div id="products" style="display:none">
					<table data-role="table" id="table-column-toggle" class="ui-responsive table-stroke">
						<thead>
					       <tr>
					         <th data-priority="1">品牌</th>
					         <th width="20%">货号</th>
					         <th width="15%">已定(手)</th>
					         <th width="12%" data-priority="2">零售价</th>
					         <th width="27%"></th>
					       </tr>
					     </thead>
					     <tbody id="productBody">
					     </tbody>
				    </table>	
				</div>
				
		</div>
		<div data-role="footer" data-theme="b" data-position="fixed" data-tap-toggle="false">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="<%=request.getContextPath()%>/orderController/StartOrder/mobile" data-icon="edit" data-ajax="false" class="ui-btn-active ui-state-persist">我要订货</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRpt/mobile" data-icon="bullets" data-ajax="false">我的订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false">品牌排名</a></li>
		      	<li></li>
		      </ul>
		     </div>
		</div> 

		<jsp:include  page="../common/Popup.jsp"/>
	</div>

</body>
</html>