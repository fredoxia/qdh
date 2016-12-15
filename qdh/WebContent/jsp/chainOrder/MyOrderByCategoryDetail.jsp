<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> 
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta http-equiv="pragma" content="no-cache">
<meta http-equiv="cache-control" content="no-cache">
<meta http-equiv="Cache" content="no-cache">
<meta http-equiv="expires" content="0"> 
<meta name="viewport" content ="width=device-width, initial-scale=1">

<%@ include file="../common/JQMStyle.jsp"%>
</head>
<body>
<!-- 按照品牌汇总的 MyOrder 汇总 -->
	<div id="myOrder" data-role="page">
		<script>
		    function myOrderByCategory(pbId, quantity){
				
				$.mobile.loading("show",{ theme: "b", text: "正在加载数据", textonly: false});
				var params="pbId=" + pbId + "&quantity=" + quantity +"&categoryId="+$("#categoryId").val();
	
				$.post('<%=request.getContextPath()%>/orderController/MyOrderMoreByCategory/mobile', params, 
				function(result) {
					$.mobile.loading("hide");
					if (result.success) {
						var resultData = result.obj;
						var myQ = resultData.myQ;
						var mySum = resultData.mySum;
						var pQ = resultData.pQ;
						var pSum = resultData.pSum;
						$("#pQ" + pbId).html(pQ);
						$("#pSum" + pbId).html(pSum);
						$("#myQ1").html(myQ);
						$("#mySum1").html(mySum);
						$("#myQ2").html(myQ);
						$("#mySum2").html(mySum);
					} else if (result.returnCode == WARNING){
						var resultData = result.obj;
						var myQ = resultData.myQ;
						var mySum = resultData.mySum;
						$("#myQ1").html(myQ);
						$("#mySum1").html(mySum);
						$("#myQ2").html(myQ);
						$("#mySum2").html(mySum);
						
						$("#pRow"+pbId).remove(); 
					} else {
						renderPopup("系统错误",result.msg)
					}
				}, 'JSON');
		    }
		    function deductOrder(pbId){
		    	myOrderByCategory(pbId, -1);
		    }
			function addOrder(pbId){
				myOrderByCategory(pbId, 1);
			}
			
			function changeCategory(categoryId){
				var params = "category_ID=" +categoryId;
				window.location.href = '<%=request.getContextPath()%>/rptController/CustRptByCategory/mobile?' + params;
			}

		</script>
		<jsp:include  page="../common/MobileHeader.jsp"/>

		<div  data-role="content" class="content">
			<form id="form1" name="form1">
		    	<form:select id="categoryId"  path="category.category_ID" items="${categories}" itemLabel="category_Name" itemValue="category_ID" onchange="changeCategory(this.value);"></form:select> 
			</form>
			<table data-role="table" id="table-column-toggle" class="ui-responsive table-stroke">
			     <thead>
			       <tr>
			         <th data-priority="1">品牌</th>
			         <th>货号</th>
			         <th>数量(手)</th>
			         <th data-priority="2">总售价</th>
			         <th></th>
			       </tr>
			     </thead>
			     <tbody>
					<c:if test="${fn:length(orderByCategory) == 0}">
						<td colspan="5">暂时还没有订货数据</td>				
					</c:if>
					<c:if test="${fn:length(orderByCategory) > 0 && not empty orderByCategoryFooter}">
						<tr>
							<th></th>
							<th>合计</th>
						    <th id="myQ1">${orderByCategoryFooter.quantity}</th>
							<th id="mySum1">${orderByCategoryFooter.sumRetailPrice}</th>
							<th width="27%"></th>
						</tr>
					</c:if>
					<c:forEach items="${orderByCategory}" var="cop">
						<tr id="pRow${cop.pbId}">
							<td style="vertical-align:middle;">${cop.brand}</td>
						    <td style="vertical-align:middle;">${cop.productCode} ${cop.color}</td>
						    <td style="vertical-align:middle;" id="pQ${cop.pbId}">${cop.quantity}</td>
							<td style="vertical-align:middle;" id="pSum${cop.pbId}">${cop.sumRetailPrice}</td>
							<td style="vertical-align:middle;">
								<div data-role="controlgroup" data-type="horizontal">
									<input type="button" value="加订" data-mini="true" data-inline="true" onclick="addOrder(${cop.pbId});"/>
									<input type="button" value="减订" data-mini="true" data-inline="true" onclick="deductOrder(${cop.pbId});"/>
								</div>
							</td>
						</tr>
					</c:forEach>
					<c:if test="${fn:length(orderByCategory) > 0 && not empty orderByCategoryFooter}">
						<tr>
							<th></th>
							<th>合计</th>
						    <th id="myQ2">${orderByCategoryFooter.quantity}</th>
							<th id="mySum2">${orderByCategoryFooter.sumRetailPrice}</th>
							<th></th>
						</tr>
					</c:if>
			     </tbody>
			  </table>				
		</div>


		<div data-role="footer" data-theme="b" data-position="fixed" data-tap-toggle="false">
			<div data-role="navbar">
		      <ul>
		      	<li><a href="<%=request.getContextPath()%>/orderController/StartOrder/mobile" data-icon="edit" data-ajax="false">我要订货</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRpt/mobile" data-icon="bullets" data-ajax="false">品牌订单</a></li>
		      	<li><a href="<%=request.getContextPath()%>/rptController/CustRptByCategory/mobile" data-icon="bars" data-ajax="false" class="ui-btn-active ui-state-persist">类别订单</a></li>
		      	<li><a id="brandRankFooter" href="<%=request.getContextPath()%>/rptController/GenerateProdRpt/mobile" data-icon="star"  data-ajax="false">品牌排名</a></li>
		      </ul>
		     </div>
		</div> 
		<jsp:include  page="../common/Popup.jsp"/>

	</div>

</body>
</html>