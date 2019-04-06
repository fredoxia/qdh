<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-Strict.dtd">
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>  
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
<meta http-equiv="X-UA-Compatible" content="IE=8" />
<title>朴与素连锁店管理信息系统</title>
<%@ include file="../../../common/Style.jsp"%>
<script>
var dataGrid ;
$(document).ready(function(){
	parent.$.messager.progress('close'); 
	
	var params = $.serializeObject($('#basicDataForm'));

	dataGrid = $('#dataGrid').datagrid({
		url : '<%=request.getContextPath()%>/prodOptController/GetBasicData',
		queryParams: params,
		fit : true,
		border : false,
		pagination : true,
		pageSize : 15,
		pageList : [ 15, 30],
		checkOnSelect : false,
		selectOnCheck : false,
		singleSelect:true,
		nowrap : false,
		rownumbers : true,
		sortName : 'numPerHand',
		sortOrder : 'asc',
		columns : [ [ {
				field : 'numPerHand',
				title : '齐码数量',
				width : 80,
				sortable:true
			    }, {
				field : 'action',
				title : '编辑',
				width : 60,
				formatter : function(value, row, index) {
					var str = '';
					str += $.formatString('<a href="#" onclick="EditNumPerHand(\'{0}\');"><img border="0" src="{1}" title="编辑齐码数量"/></a>', row.id, '<%=request.getContextPath()%>/conf_files/easyUI/themes/icons/text_1.png');
					return str;
				}
		}]],
		toolbar : '#toolbar',
	});

});

function EditNumPerHand(numId){
	var params = "basicData=numPerHand";
	if (numId != 0)
	   params += "&basicDataId=" + numId;

	$.modalDialog.opener_dataGrid = dataGrid;
	
	$.modalDialog({
		title : "添加/更新齐码数量",
		width : 540,
		height : 380,
		modal : false,
		draggable:false,
		href : '<%=request.getContextPath()%>/prodOptController/PreAddBasicData?' + params,
		buttons : [ {
			text : '提交信息',
			handler : function() {
				var f = $.modalDialog.handler.find('#updateNumPerHandForm');
				f.submit();
			}
		} ]	
	});
}
function refresh(){
	$('#dataGrid').datagrid('reload');
}
</script>
</head>
<body>
	<div class="easyui-layout" data-options="fit : true,border : false">
		<div data-options="region:'north',border:false" style="height: 60px;">
			<%@ include file="ChooseBasicData.jsp"%>
		</div>
		<div data-options="region:'center',border:false">
				<table id="dataGrid">			       
		        </table>

			<div id="toolbar" style="display: none;">
		             <a onclick="EditNumPerHand(0);" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-add'">添加</a>
                     <a onclick="refresh();" href="javascript:void(0);" class="easyui-linkbutton" data-options="plain:true,iconCls:'icon-reload'">刷新</a>
	        </div>
		</div>
	</div>
</body>
</html>