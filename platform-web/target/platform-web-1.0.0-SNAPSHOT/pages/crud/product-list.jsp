<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@ include file="/pages/common/header.jsp"%>
<script type="text/javascript">
	var gridManager;
	var _dialog;
	$(function(){
		PageLoader.initSerachPanel();
		PageLoader.initGridPanel();
	});
	
	PageLoader = {
		initSerachPanel:function(){
			$("input[id^='product_dateID_']").ligerDateEditor({initValue:new Date().Format("yyyy-MM-dd")});//加载日期选择器
		},
		initGridPanel:function(){
			gridManager = $("#maingrid").ligerGrid({
	            height:'100%',
	            checkbox:true,
	            columns: [
	            { display: '产品名', name: 'product_name', align: 'left', width:'40%',minWidth: 100 },
	            { display: '生产日期', name: 'product_date', width:'50%',minWidth: 120 },
	            { display: '操作', isAllowHide: false, width:'10%',
	                render: function (rowdata, rowindex, value)
	                {
	                    var html = '<a href="#" onclick="openDetailsDialog(' + rowdata.id + ')">详细</a>';
	                    return html;
	                }
	            }
	            ], 
	            url:'/product/pageJson.do',
	            height:"400px",
	            pageSize:3 ,
	            rownumbers:false,
	            toolbar: { items: [
	            { id:'add',text: '增加', click: itemclick, icon: 'add' },
	            { line: true },
	            { id:'modify',text: '修改', click: itemclick, icon: 'modify' },
	            { line: true },
	            { id:'delete',text: '删除', click: itemclick, icon: 'delete'}
	            ]
	            }
	        });
		}
	};
	
	function itemclick(item){
		console.info(item);
		if("add" == item.id){
			openAddDialog();
		} else if("modify" == item.id){
			openModifyDialog();
		} else if("delete" == item.id){
			deleteDataAction();
		}
	}
	
	function openDetailsDialog(id){
		var url = "product-detail.jsp?id=" + id;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'详细',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	
	function openAddDialog(){
		var url = "product-add.jsp";
		_dialog = jQuery.ligerDialog.open({
	  	    title:'新增',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	
	function openModifyDialog(){
		var newRow = gridManager.getSelected();
	     if (!newRow) { alert('请选择行'); return; }
	     var i = 0;
	     $.each(gridManager.getSelectedRows(), function(index, element) {
			  i++;
		  });
	     if(i>1){
	    	 alert('请只选择一行'); return;
	     }
	     id = newRow.id;
	     console.info(JSON.stringify(newRow));
	     url = "product-update.jsp?id=" + newRow.id;
	     
	     _dialog = jQuery.ligerDialog.open({
		  	    title:'修改',
		  	    url:url,
		  	    isResize: true, width: 550, height: 550, isHidden: false
		     });
	}
	
	function deleteDataAction(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		var removeData = "";
		$.each(gridManager.getSelectedRows(), function(index, element) {
			removeData = removeData + element.id + ","; 
		});
		removeData = removeData.substr(0, removeData.length-1);
		$.post("${pageContext.request.contextPath}/product/remove.do", 
				{'ids':removeData},
				function(json) {
					if(json && json.result){
					 alert(json.result);
					 gridManager.loadData();
					}
				});
		
	}
	
	function searchAction(){
		var param=$("#searchForm").serialize();
		gridManager.loadServerData(param);/*执行服务器查询*/
	}
</script>
<title>Insert title here</title>
</head>
<body>
<!-- search form -->
<form name="searchForm" id="searchForm" target="_self">
<input type="hidden" name="page" value="1">
<input type="hidden" name="pagesize" value="3">
<table border="0" cellspacing="0" cellpadding="0">
  <tr>
    <td>
<table width="800" border="0" style="margin:5px">
 <tr height="25px">
  <td width="12%">product_name:</td>
  <td width="22%"><input name="product_name" class="input-common" type="text" id="product_nameID"  /></td>
  <td width="12%">product_date:</td>
  <td colspan="3">
  	 <table border="0" cellspacing="0" cellpadding="0">
      <tr>
        <td><input type="text" name="product_date" id="product_dateID_start" ></td>
        <td width="15px">&nbsp;-&nbsp;</td>
        <td><input type="text" name="product_dateEnd" id="product_dateID_end" ></td>
      </tr>
    </table>
  	</td>
</tr>
</table>	
</td>
    <td><input id="searchButton" type="button" class="btn-normal" onclick="searchAction()" value="查询" /></td>
  </tr>
</table>	
</form>
<div id="maingrid"></div>
</body>
</html>