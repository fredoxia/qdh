<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script>
var layout_west_tree;
var layout_west_tree2;
$(function() {
		layout_west_tree = $('#treeMenu').tree({
			onClick : function(node) {
				if (node.attributes && node.attributes.url) {
					var url;
					url = node.attributes.url;
					parent.$.messager.progress({
								title : '提示',
								text : '数据处理中，请稍后....'
							});

     				addTab4({
						url : url,
						title : node.text,
						iconCls : node.iconCls
					});

				}
			},
			onLoadSuccess : function(node, data) {
				parent.$.messager.progress('close');
			}
		});
	}); 
</script>
<div id="menuAccordian" class="easyui-accordion" style="fit:true,border:false">  
    <div title="系统菜单" data-options="selected:true" style="padding:10px;">
        <ul id="treeMenu" class="easyui-tree" lines="true">  
               <li data-options="iconCls:'icon-images',state:'open',border:false">  
		            <span>订货会资料管理</span>  
	        		<ul>
	        			 <li data-options="iconCls:'icon-images',attributes:{url:'<%=request.getContextPath()%>/prodOptController/HQProdMgmt'}">订货会品牌管理</li>
	        			 <li data-options="iconCls:'icon-images',attributes:{url:'<%=request.getContextPath()%>/prodOptController/HQBarcodeMgmt'}">当前条码查询</li>
	        			 <li data-options="iconCls:'icon-images',attributes:{url:'<%=request.getContextPath()%>/custAcctController/CustAcctMgmt'}">客户资料管理</li>
	        		</ul> 
		        </li>
		</ul>

	</div>  
 
</div>
