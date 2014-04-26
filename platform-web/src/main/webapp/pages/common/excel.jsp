<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
<!-- ajaxfileupload样式 -->
<link href="${pageContext.request.contextPath}/css/jquery-fileupload/ajaxfileupload.css" rel="stylesheet" type="text/css" />
<!-- ajaxfileupload -->
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-fileupload/ajaxfileupload.js"></script>
<!--jquery-fileupload样式  -->
<link href="${pageContext.request.contextPath}/css/bootstrap.min.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/jquery-fileupload/jquery.fileupload.css" rel="stylesheet" type="text/css" />
<link href="${pageContext.request.contextPath}/css/jquery-fileupload/jquery.fileupload-ui.css" rel="stylesheet" type="text/css" />
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-fileupload/vendor/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-fileupload/jquery.iframe-transport.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-fileupload/jquery.fileupload.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-fileupload/cors/jquery.xdr-transport.js"></script>
<script type="text/javascript">
	/**
	excel导出公共页
	*/
	//加载grid全部数据
	function loadExcelGridData(url,data){
		var result;
		$.ajax({
		      url: url,
		      data:data,
		      async:false,
		      type: "POST",
		      success: function(json){
		    	 if(json.error){
					 alert(json.error);
					 return;
				 }
		         if(json){
		        	 result = json;
		         }
		      }
		   }
		);
		return result.Rows;
	}
	
	//excel全部导出时,将全部数据显示在grid中
	function setAllDataExcelGrid(gridManager,gridbodytable){
	    //导出全部
	    //获得grid相关信息
	    if("server" == gridManager.options.dataAction){//grid 为server加载数据方式
	        var total = gridManager.options.total;
			var pageParmName = gridManager.options.pageParmName;
			var pagesizeParmName = gridManager.options.pagesizeParmName;
			var parms = gridManager.options.parms;
			
			var url = gridManager.options.url;
			var data = [];
			data.push({name:pageParmName,value:1});
			data.push({name:pagesizeParmName,value:total});
			//获得全部数据
			var gridData = loadExcelGridData(url,data);
	    } else if("local" == gridManager.options.dataAction){//grid 为local加载数据方式
	    	gridData = gridManager.options.data.Rows;
	    }
		if(!gridData){
			alert("加载全部数据出错");
			return;
		}
	    //获得一行，做为行模板
	    var gridrow = $(".l-grid-row:first",gridbodytable);
	    
	    gridbodytable.empty();
	    
	    var columns;
	    //多行列头
	    if(gridManager._columnMaxLevel > 1){
	    	columns = gridManager.options.columns[0].columns;
	    } else {
	    	columns = gridManager.options.columns;
	    }
	    //获得每行数据
	    for(var i=0;i<gridData.length;i++){
	        //修改行数据
	    	$(".l-grid-row-cell-inner",gridrow).each(function(index,elem){
	    		//获得每列的，数据字段名gridManager.options.columns[0].columns[index].name
	    		//获得每列数据，修改相应单元格
	    		
	        	$(elem).text(gridData[i][columns[index].name]);
	        });
	        //添加行
	        gridbodytable.append("<tr class='l-grid-row'>"+ gridrow.html() +"</tr>");
	    }
	}
	/**
	以excle方式，导出grid
	fileName excel文件名
	gridManager grid对象
	all true代表导出grid全部数据,否则只导出将前页
	*/
	function exportExcel(fileName,gridManager,all){
		
		var grid = $(gridManager.element);
		
		var gridview = $(".l-grid:first",grid);
	
		var gridview1 = $(".l-grid1:first", gridview);
	    var gridview2 = $(".l-grid2:first", gridview);
	    //将grid复制到临时层中
	    var excelView = $("#exportExcelDiv");
	    excelView.empty();
	    excelView.html(gridview2.html());
	
	    //添加边框线
	    var gridheadertable = $(".l-grid-header-table",excelView);
	    gridheadertable.attr("border","1");
	    var gridbodytable = $(".l-grid-body-table",excelView);
	    gridbodytable.attr("border","1");
	    
	    //excel全部导出时,将全部数据显示在grid中
	    if(all){
	    	setAllDataExcelGrid(gridManager,gridbodytable);
	    }
	    
	    //表头     
	    var gridheader = $(".l-grid-header:first", excelView);
	    //表主体     
	    var gridbody = $(".l-grid-body:first", excelView);
	    
	    //grid有序号，为html添加序号td
	    if(gridManager.options.rownumbers){
	        $(".l-grid-hd-row",gridheader).each(function(index,elem){
	        	$(elem).prepend("<td id='tempTd'></td>");
	        });
	        
	        $(".l-grid-row",gridbody).each(function(index,elem){
	        	$(elem).prepend("<td id='tempTd'>"+ (index+1) +"</td>");
	        });
	        
	        $(".l-grid-totalsummary",gridbody).each(function(index,elem){
	        	$(elem).prepend("<td id='tempTd'></td>");
	        });
	    }
	    
		$("#excel").attr("action","${pageContext.request.contextPath}/excel/exportByHtml.do");
		$("#excel input[name='fileName']").val(fileName);
		$("#excel input[name='grid']").val(gridheader.html()+gridbody.html());
		$("#excel").submit();
		/*
		//删除序号td
		if(gridManager.options.rownumbers){
			$("td#tempTd").each(function(index,elem){
	        	elem.remove();
	        });
		}
		//删除边框线
		$(".l-grid-header-table",excelView).attr("border","0");
	    $(".l-grid-body-table",excelView).attr("border","0");
	    */
	    //清空临时层
		excelView.empty();
	}
	/**
	通过json数据格式导出excel
	*/
	function exportExcelByData(fileName,gridManager,all){
		var grid = $(gridManager.element);
		
		var gridview = $(".l-grid:first",grid);
	
		var gridview1 = $(".l-grid1:first", gridview);
	    var gridview2 = $(".l-grid2:first", gridview);
	    //将grid复制到临时层中
	    var excelView = $("#exportExcelDiv");
	    excelView.empty();
	    excelView.html(gridview2.html());
	
	    //添加边框线
	    var gridheadertable = $(".l-grid-header-table",excelView);
	    gridheadertable.attr("border","1");
	    var gridbodytable = $(".l-grid-body-table",excelView);
	    gridbodytable.attr("border","1");
	    
	    //excel全部导出时,将全部数据显示在grid中
	    if(all){
	    	setAllDataExcelGrid(gridManager,gridbodytable);
	    }
	    
	    //表头     
	    var gridheader = $(".l-grid-header:first", excelView);
	    //表主体     
	    var gridbody = $(".l-grid-body:first", excelView);
	    
	    //grid有序号，为html添加序号td
	    if(gridManager.options.rownumbers){
	        $(".l-grid-hd-row",gridheader).each(function(index,elem){
	        	$(elem).prepend('<td class="l-grid-hd-cell" style="width: 50px;"><div class="l-grid-hd-cell-inner"><span class="l-grid-hd-cell-text"></span></div></td>');
	        });
	        
	        $(".l-grid-row",gridbody).each(function(index,elem){
	        	$(elem).prepend('<td class="l-grid-row-cell" style="width: 50px;"><div class="l-grid-row-cell-inner" style="width: 50px; height: 22px;">'+ (index+1) +'</div></td>');
	        });
	        
	        $(".l-grid-totalsummary",gridbody).each(function(index,elem){
	        	$(elem).prepend('<td class="l-grid-totalsummary-cell" width="50" columnname="name"><div class="l-grid-totalsummary-cell-inner" style="text-Align: left;"></div></td>');
	        });
	    }
	    
	    var rows = [];
	    //将列头部分转换为json
	    $("#exportExcelDiv .l-grid-header-inner").find(".l-grid-hd-row").each(function(rowIdx,rowElem){
	    	var row = {};
	    	var cells = [];
	    	//列头中的每个格
	    	$(rowElem).find(".l-grid-hd-cell").each(function(colIdx,colElem){
	    		var cell = {};
	    		if($(colElem).attr("columnname")){
	    			cell["columnname"] = $(colElem).attr("columnname");
	    		} 
	    		if($(colElem).find(".l-grid-hd-cell-text").text()){
	    			cell["text"] = $(colElem).find(".l-grid-hd-cell-text").text();
	    		}
	    		if($(colElem).attr("colspan")){
	    			cell["colspan"] = $(colElem).attr("colspan");
	    		}
	    		if($(colElem).find(".l-grid-hd-cell-inner").css("text-align")){
	    			cell["align"] = $(colElem).find(".l-grid-hd-cell-inner").css("text-align");
	    		} else {
	    			cell["align"] = "center";
	    		}
	    		cell["valign"] = "center";
	    		if($(colElem).css("width")){
	    			cell["width"] = $(colElem).css("width");
	    		}
	    		if($(colElem).css("height")){
	    			row["height"] = $(colElem).css("height");
	    		}
	    		cells.push(cell);
	    	});
	    	row["cell"] = cells;
	    	rows.push(row);
	    });
	    //将正文部分转为json
	    $("#exportExcelDiv .l-grid-body-inner").find(".l-grid-row").each(function(rowIdx,rowElem){
	    	var row = {};
	    	var cells = [];
	    	$(rowElem).find(".l-grid-row-cell").each(function(colIdx,colElem){
	    		var cell = {};
	    		if($(colElem).attr("columnname")){
	    			cell["columnname"] = $(colElem).attr("columnname");
	    		} 
	    		if($(colElem).find(".l-grid-row-cell-inner").text()){
	    			cell["text"] = $(colElem).find(".l-grid-row-cell-inner").text();
	    		}
	    		if($(colElem).attr("colspan")){
	    			cell["colspan"] = $(colElem).attr("colspan");
	    		}
	    		if($(colElem).find(".l-grid-row-cell-inner").css("text-align")){
	    			cell["align"] = $(colElem).find(".l-grid-row-cell-inner").css("text-align");
	    		} else {
	    			cell["align"] = "center";
	    		}
	    		cell["valign"] = "center";
	    		if($(colElem).css("width")){
	    			cell["width"] = $(colElem).css("width");
	    		}
	    		if($(colElem).find(".l-grid-row-cell-inner").css("height")){
	    			row["height"] = $(colElem).find(".l-grid-row-cell-inner").css("height");
	    		}
	    		cells.push(cell);
	    	});
	    	row["cell"] = cells;
	    	rows.push(row);
	    });
	    //将汇总部分转为json
	    $("#exportExcelDiv .l-grid-body-inner").find(".l-grid-totalsummary").each(function(rowIdx,rowElem){
	    	var row = {};
	    	var cells = [];
	    	$(rowElem).find(".l-grid-totalsummary-cell").each(function(colIdx,colElem){
	    		var cell = {};
	    		if($(colElem).attr("columnname")){
	    			cell["columnname"] = $(colElem).attr("columnname");
	    		} 
	    		if($(colElem).find(".l-grid-totalsummary-cell-inner").text()){
	    			cell["text"] = $(colElem).find(".l-grid-totalsummary-cell-inner").text();
	    		}
	    		if($(colElem).attr("colspan")){
	    			cell["colspan"] = $(colElem).attr("colspan");
	    		}
	    		if($(colElem).find(".l-grid-totalsummary-cell-inner").css("text-align")){
	    			cell["align"] = $(colElem).find(".l-grid-totalsummary-cell-inner").css("text-align");
	    		} else {
	    			cell["align"] = "center";
	    		}
	    		cell["valign"] = "center";
	    		if($(colElem).css("width")){
	    			cell["width"] = $(colElem).css("width");
	    		}
	    		cells.push(cell);
	    	});
	    	row["cell"] = cells;
	    	rows.push(row);
	    });
		/*
		//删除序号td
		if(gridManager.options.rownumbers){
			$("td#tempTd").each(function(index,elem){
	        	elem.remove();
	        });
		}
		//删除边框线
		$(".l-grid-header-table",excelView).attr("border","0");
	    $(".l-grid-body-table",excelView).attr("border","0");
	    */
	    //清空临时层
		excelView.empty();
	
		$("#excel").attr("action","${pageContext.request.contextPath}/excel/exportByData.do");
		$("#excel input[name='fileName']").val(fileName);
		$("#excel input[name='grid']").val(JSON.stringify(rows));
		$("#excel").submit();
	}
	/**
	只导出grid表头部分
	*/
	function exportExcelHead(fileName,gridManager){
		var grid = $(gridManager.element);
		
		var gridview = $(".l-grid:first",grid);
	
		var gridview1 = $(".l-grid1:first", gridview);
	    var gridview2 = $(".l-grid2:first", gridview);
	    //将grid复制到临时层中
	    var excelView = $("#exportExcelDiv");
	    excelView.empty();
	    excelView.html(gridview2.html());
	    
	    var rows = [];
	    //将列头部分转换为json
	    $("#exportExcelDiv .l-grid-header-inner").find(".l-grid-hd-row").each(function(rowIdx,rowElem){
	    	var row = {};
	    	var cells = [];
	    	//列头中的每个格
	    	$(rowElem).find(".l-grid-hd-cell").each(function(colIdx,colElem){
	    		var cell = {};
	    		if($(colElem).attr("columnname")){
	    			cell["columnname"] = $(colElem).attr("columnname");
	    		} 
	    		if($(colElem).find(".l-grid-hd-cell-text").text()){
	    			cell["text"] = $(colElem).find(".l-grid-hd-cell-text").text();
	    		}
	    		if($(colElem).attr("colspan")){
	    			cell["colspan"] = $(colElem).attr("colspan");
	    		}
	    		if($(colElem).find(".l-grid-hd-cell-inner").css("text-align")){
	    			cell["align"] = $(colElem).find(".l-grid-hd-cell-inner").css("text-align");
	    		} else {
	    			cell["align"] = "center";
	    		}
	    		cell["valign"] = "center";
	    		if($(colElem).css("width")){
	    			cell["width"] = $(colElem).css("width");
	    		}
	    		if($(colElem).css("height")){
	    			row["height"] = $(colElem).css("height");
	    		}
	    		cells.push(cell);
	    	});
	    	row["cell"] = cells;
	    	rows.push(row);
	    });
	    //清空临时层
		excelView.empty();
	
		$("#excel").attr("action","${pageContext.request.contextPath}/excel/exportByData.do");
		$("#excel input[name='fileName']").val(fileName);
		$("#excel input[name='grid']").val(JSON.stringify(rows));
		$("#excel").submit();
	}
	
	/**
	上传excel
	*/
	function uploadExcel(gridManager,url,fileId){
		 //$("#loading").ajaxStart(function(){$(this).show();}).ajaxComplete(function(){$(this).hide();});
		if(!url){
			//url = '${pageContext.request.contextPath}/excel/upload.do';
			url = '${pageContext.request.contextPath}/excel/uploadExcel.do';
		}
		if(!fileId){
			fileId = 'fileUpload';
		}
		//执行上传文件操作的函数  
	    $.ajaxFileUpload({  
	        //处理文件上传操作的服务器端地址(可以传参数,已亲测可用)  
	        url:url,  
	        secureuri:false,                           //是否启用安全提交,默认为false   
	        fileElementId:fileId,               //文件选择框的id属性  
	        dataType:'text',                           //服务器返回的格式,可以是json或xml等  
	        success:function(data, status){            //服务器响应成功时的处理函数  
				data = data.replace(/<pre.*?>/g, '');  //ajaxFileUpload会对服务器响应回来的text内容加上<pre style="....">text</pre>前后缀  
				data = data.replace(/<PRE.*?>/g, '');  
				data = data.replace("<PRE>", '');  
				data = data.replace("</PRE>", '');  
				data = data.replace("<pre>", '');  
				data = data.replace("</pre>", '');  
	            data = JSON.parse(data);
	            if("success" == data.status){
	            	excelDataToGrid(gridManager,data.data);//将excel上传数据加载到grid中
	            	$.ligerDialog.success(data.result);
	            } else {
	            	$.ligerDialog.warn(data.result);
	            }
	        },  
	        error:function(data, status, e){ //服务器响应失败时的处理函数  
	        	$.ligerDialog.error("文件上传失败!!");
	        }  
	    });
		$('#fileUpload').replaceWith('<input type="file" id="fileUpload" name="file"/>');
		$('#fileUpload').live('change',function(){
			uploadExcel(gridManager,url,fileId);
		});
	}
	/**
	将excel数据加载到grid中
	*/
	/*
	1/toolbar或页面中添加
	'<span class="fileinput-button">' +
	'<span>导入excel</span>'+
	'<input type="file" id="fileUpload" name="file" class="input"/>'+
	'</span>'
	2/页加载事件中添加
	$('#fileUpload').live('change',function(){
		uploadExcel(gridManager);
	});
	*/
	function excelDataToGrid(gridManager,data){
		var columns;
	    //多行列头
	    if(gridManager._columnMaxLevel > 1){
	    	columns = gridManager.options.columns[0].columns;
	    } else {
	    	columns = gridManager.options.columns;
	    }
	    
	    var excelData = JSON.parse(data);
	    //利用grid列信息及上传的数据，构建grid需要的数据格式
	    var rowdata = [];
	    for(var i=gridManager._columnMaxLevel;i<excelData.length;i++){
	    	var row = {};
	    	for(var j=0;j<excelData[i].length;j++){
	    		row[columns[j].name] = excelData[i][j];
	    	}
	    	rowdata.push(row);
	    }
	    var data = {};
	    data["Rows"] = rowdata;
		//初始化分页设置
		gridManager.options.newPage = 1;
		gridManager.options.total = 0;
		gridManager.options.dataAction = "local";
		//将数据加载到grid中
		gridManager.loadData(data);
	    
	}
</script>
<iframe name="excelFrame" style="display:none;"></iframe>
<div id="exportExcelDiv"></div>
<form id="excel" style="display:none;" target="excelFrame" method="post" action="">
	<input type="hidden" name="fileName">
	<input type="hidden" name="grid">
</form>
