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
	var gridData;
	$(function(){
		PageLoader.initSerachPanel();
		PageLoader.initGridData();
		PageLoader.initGridPanel();
	});
	
	PageLoader = {
		initSerachPanel:function(){
			$("input[id^='product_dateID_']").ligerDateEditor({initValue:new Date().Format("yyyy-MM-dd")});//加载日期选择器
		},
		initGridData:function(){
			$.ajax({
			      url: "${pageContext.request.contextPath}/product/find.do",
			      type: "POST",
			      async:false,
			      success: function(json){
			         if(json){
			        	 gridData = json;
			         }
			      }
			   }
			);
		},
		initGridPanel:function(){
			console.info(gridData);
			gridManager = $("#maingrid").ligerGrid({
				isScroll:false,
	            checkbox:true,
	            data:gridData,
	            usePager: false,
	            enabledEdit: true,clickToEdit:false,isScroll: false,
	            width:'100%',
	            columns: [
	            { display: '产品名', name: 'product_name', align: 'left', width:400,minWidth: 100,editor: { type: 'text' } },
	            { display: '生产日期', name: 'product_date', width:300,minWidth: 120 ,type: 'date',format: 'yyyy-MM-dd',editor: { type: 'date'}},
	            { display: '操作', isAllowHide: false, width:100,
	                render: function (rowdata, rowindex, value)
	                {
	                	 var h = "";
	                     if (!rowdata._editing)
	                     {
	                         h += "<a href='javascript:beginEdit(" + rowindex + ")'>修改</a> ";
	                         h += "<a href='javascript:deleteRow(" + rowindex + ")'>删除</a> "; 
	                     }
	                     else
	                     {
	                         h += "<a href='javascript:endEdit(" + rowindex + ")'>提交</a> ";
	                         h += "<a href='javascript:cancelEdit(" + rowindex + ")'>取消</a> "; 
	                     }
	                     return h;
	                }
	            }
	            ],
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
	
	 function beginEdit(rowid) { 
		 gridManager.beginEdit(rowid);
     }
     function cancelEdit(rowid) { 
    	 gridManager.cancelEdit(rowid);
     }
     function endEdit(rowid)
     {
    	 gridManager.endEdit(rowid);
    	 updateDataAction(rowid);
     }
     
     function itemclick(item){
 		console.info(item);
 		if("add" == item.id){
 			addNewRow();
 		} else if("modify" == item.id){
 			updateRow();
 		} else if("delete" == item.id){
 			deleteDataAction();
 		}
 	}
     
     function deleteRow(rowid)
     {
    	 deleteDataAction(rowid);
     }
	
	function addNewRow(){
		gridManager.addEditRow();
	}
	
	function updateRow(){
		var newRow = gridManager.getSelected();
		if (!newRow) { alert('请选择行'); return; }
		gridManager.beginEdit(newRow);
	}
	
	function openDetailsDialog(id){
		var url = "product-detail.jsp?id=" + id;
		_dialog = jQuery.ligerDialog.open({
	  	    title:'详细',
	  	    url:url,
	  	    isResize: true, width: 550, height: 550, isHidden: false
	     });
	}
	
	function updateDataAction(rowid){
		gridManager.getRow(rowid)["product_date"] = gridManager.getRow(rowid)["product_date"].Format("yyyy-MM-dd");
	   	 if("update" == gridManager.getRow(rowid)[gridManager.options.statusName]){
	   		 $.post("${pageContext.request.contextPath}/product/update.do", 
	   					gridManager.getRow(rowid),
	   					function(json) {
	   						if(json && json.result){
	   							MessageBox.sucess("pf00001");
	   						}
	   				});
	   	 } else if("add" == gridManager.getRow(rowid)[gridManager.options.statusName]){
	   		 $.post("${pageContext.request.contextPath}/product/add.do", 
	   				 	gridManager.getRow(rowid),
	   					function(json) {
	   						if(json && json.result){
	   						 MessageBox.sucess("pf00001");
	   						 gridManager.getRow(rowid)["id"] = json.data.id;//获得该行主键值
	   						}
	   					});
	   		 gridManager.getRow(rowid)[gridManager.options.statusName]="";//清除状态值，以便下次可以更新
	   		 
	   	 }
	}
	
	function deleteDataAction(rowid){
		var removeData = "";
		if(undefined == rowid){
			var newRow = gridManager.getSelected();
			if (!newRow) { MessageBox.warn('请选择行'); return; }
			MessageBox.confirm("确定删除?",function(flag){
				if(flag){
					$.each(gridManager.getSelectedRows(), function(index, element) {
						removeData = removeData + element.id + ","; 
					});
					removeData = removeData.substr(0, removeData.length-1);
					deletePostAction(removeData);
				}
			});
		} else {
			MessageBox.confirm("确定删除?",function(flag){
				if(flag){
					console.info("ddd");
					removeData = gridManager.getRow(rowid)["id"];
					deletePostAction(removeData,rowid);
				}
			});
		}
	}
	function deletePostAction(removeData,rowid){
		$.post("${pageContext.request.contextPath}/product/remove.do", 
				{'ids':removeData},
				function(json) {
					if(json && json.result){
					 MessageBox.sucess("pf00001");
					 //gridManager.loadData();
					}
				});
		if(undefined == rowid){
			gridManager.removeRange(gridManager.getSelectedRows());
		} else {
			gridManager.remove(gridManager.getRow(rowid));
		}
	}
	
	function searchAction(){
		var param=$("#searchForm").serialize();
		$.ajax({
		      url: "${pageContext.request.contextPath}/product/find.do",
		      type: "POST",
		      data: param,
		      async:false,
		      success: function(json){
		         console.info(json);
		    	 
		       	 gridData = json;
		       	 
		       	 gridManager.sortedData = gridData;//初始化排序数据
		    	 gridManager.loadData(gridData);//初始化Grid数据
		      }
		   }
		);
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
<div class="l-clear"></div>
<div id="maingrid" style="margin-top:20px"></div>
</body>
</html>