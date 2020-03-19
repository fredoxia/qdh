<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
function changeBasicData(value){
    if (value != ""){
    	document.basicDataForm.action="<%=request.getContextPath()%>/prodOptController/ChangeBasicData";
    	document.basicDataForm.submit(); 
    }
}
</script>
<div class="easyui-panel" style="padding:5px 5px;" data-options="fit:true,border:false">
<form:form id="basicDataForm" cssClass="easyui-form" name="basicDataForm">
    <div style="margin-bottom:20px">
	 <form:select id="basicData" path="basicData" cssClass="easyui-combobox"  style="width:200px;" data-options="label:'基础资料类别:',labelWidth:'90',editable:false,onChange:function(param1, param2){changeBasicData(param1);}">
		           <form:option value="-">- 请选择 -</form:option>
		           <form:option value="year">年份</form:option>
		           <form:option value="quarter">季度</form:option>
		           <form:option value="brand">品牌</form:option>
		           <form:option value="category">货品类别</form:option>
		           <form:option value="color">颜色</form:option>
		           <form:option value="productUnit">货品单位</form:option>
		           <form:option value="numPerHand">齐手数量</form:option>
		</form:select>
     </div>
</form:form>
</div>