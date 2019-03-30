<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<script type="text/javascript">
	var dataGrid2;

	function addCust(){
	    if (!$('#form2').form('validate'))
	    	return ;
	    
		var params=$("#form2").serialize(); 
		$.post('<%=request.getContextPath()%>/custAcctController/AddCust', params , function(result) {
			if (result.success) {
				parent.$.messager.alert('提示', result.msg, 'infor');
				$("#addForm").dialog("close");
				parent.$.modalDialog.openner_dataGrid.datagrid('reload');
			} else {
				parent.$.messager.alert('失败警告', result.msg, 'error');
			}
			

		}, 'JSON');

		return false;
	}
</script>

    <div class="easyui-panel" style="padding:30px 60px;" data-options="fit:true,border:false">
        <form  id="form2" method="post"  class="easyui-form" onsubmit="return addCust();">
            <div style="margin-bottom:20px">
                <form:input cssClass="easyui-textbox" path="cust.id" style="width:70%" data-options="label:'登录帐号:',editable:false"/>
            </div>
            <div style="margin-bottom:20px">
                <form:input class="easyui-textbox" path="cust.custName" style="width:70%" data-options="label:'客户名字:',required:true,validType:'length[2,20]'"/>
            </div>
            <div style="margin-bottom:20px">
                <form:input class="easyui-textbox" path="cust.custRegion" style="width:70%" data-options="label:'客户备注:',validType:'length[0,20]'"/>
            </div>
            <div style="margin-bottom:20px">
                <form:input class="easyui-textbox" path="cust.password" style="width:40%" data-options="label:'密码:',prompt:'如果留空,系统将自动生成四位数密码',validType:'length[4,4]'"/>
            </div>
            <div style="margin-bottom:20px">
                <form:select class="easyui-combobox" path="cust.status" label="状态" style="width:70%">
                   <form:option value="0">正常</form:option>
                   <form:option value="-1">冻结</form:option>
                </form:select>
            </div>
        </form>
    </div>
