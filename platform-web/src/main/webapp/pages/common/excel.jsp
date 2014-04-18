<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="utf-8"%>
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
	    
		$("#excel").attr("action","${pageContext.request.contextPath}/excel/export.do");
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
</script>
<div id="exportExcelDiv"></div>
<form id="excel" style="display:none;" target="excelFrame" method="post" action="">
	<input type="hidden" name="fileName">
	<input type="hidden" name="grid">
</form>
