<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<script type="text/javascript">
	/**
	用户选择(组织机构形式)对话框
	*/
	/*用户选择(组织机构形式)对话框*/
	function openOrgUserDialog(callback) {
		var manager = $("#menuTree").ligerGetTreeManager();
		if (manager != null) {
			manager.clear();
		}
		$.getJSON('${pageContext.request.contextPath}/org/findOrgAndIdentityTree.do', function(menus) {
			var menuDialog = $.ligerDialog.open({
				width : 400,
				// 获取所有菜单数据
				height: 300,
				target : $("#orgTreeDiv"),
				title : "选择人员",
				content : $("#menuTree").ligerTree({
					data : menus.Rows,
					idFieldName : 'id',
					checkbox : false
				}),
				buttons : [{
					text : "确定",
					onclick : function() {
						var manager = $("#menuTree").ligerGetTreeManager();
						var node = manager.getSelected();
						if(0 == node.data.level){//只有选择用户才有效
							menuDialog.hide();
							callback(node.data);//将选择的用户传给回调方法
						}
					}
				}, {
					text : "取消",
					onclick : function() {
						menuDialog.hide();
					}
				} ]
			});
		});
	}
</script>
   <div id="orgTreeDiv">
 		<ul id="menuTree">
 		</ul>
	</div>
