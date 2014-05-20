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
	文件操作公共页
	*/
	/**
	上传文件
	*/
	/**
	1、页面添加
	<span class="l-button l-button-test fileinput-button">
	<span>上传文件</span>
	<input type="file" id="fileUpload" name="file" class="input"/>
	</span>
	2、页面加载事件中
	$('#fileUpload').live('change',function(){
		uploadFile(loadGrid);
	});	
	*/
	function uploadFile(url,fileId,callback){
		 //$("#loading").ajaxStart(function(){$(this).show();}).ajaxComplete(function(){$(this).hide();});
		if (typeof (url) == "function"){
			callback = url;
			url = null;
		}
		if(!url){
			url = '${pageContext.request.contextPath}/file/upload.do';
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
	            	$.ligerDialog.success(data.result);
	            	if(callback){
	            		callback();
	            	}
	            } else {
	            	$.ligerDialog.warn(data.error);
	            }
	        },  
	        error:function(data, status, e){ //服务器响应失败时的处理函数  
	        	$.ligerDialog.error("文件上传失败!!");
	        }  
	    });
		$('#fileUpload').replaceWith('<input type="file" id="fileUpload" name="file"/>');
		$('#fileUpload').live('change',function(){
			uploadFile(url,fileId);
		});
	}

	function downloadFile(fileName){
		$("#file").attr("action","${pageContext.request.contextPath}/file/download.do");
		$("#file input[name='fileName']").val(fileName);
		$("#file").submit();
	}

	function deleteFile(fileName,callback){
		var data = {};
		data["fileName"] = fileName;
		$.ajax({ 
		    type:"POST", 
		    url:"${pageContext.request.contextPath}/file/deleteFile.do", 
		    data:data,
		    success:function(json){ 
		    	if(json.error){
		    		$.ligerDialog.error(json.error);
				}
				if(json && json.result){
					$.ligerDialog.success(json.result);
					if(callback){
	            		callback();
	            	}
				}
		    } 
		 });
	}
</script>
<iframe name="fileFrame" style="display:none;"></iframe>
<form id="file" style="display:none;" target="fileFrame" method="post" action="">
	<input type="hidden" name="fileName">
</form>
