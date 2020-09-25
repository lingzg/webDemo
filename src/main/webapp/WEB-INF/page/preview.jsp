<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
<title>FilePreview test</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<script type="text/javascript" src="${contextPath}/static/js/pdf.js"></script>
<%-- <link href="${contextPath}/static/css/video-js.css" rel="stylesheet">  
<script src="${contextPath}/static/js/video.js"></script>  --%>
<link href="https://cdnjs.cloudflare.com/ajax/libs/video.js/7.3.0/video-js.min.css" rel="stylesheet">
<script src="https://cdnjs.cloudflare.com/ajax/libs/video.js/7.3.0/video.min.js"></script>
</head>
<body>

	<div id="example1"></div>
	<video id="my_video_1" class="video-js vjs-default-skin" controls preload="auto" width="275" height="200" poster="" data-setup="{}">  
	    <source src="${contextPath}/static/beb44bc483fd6c9ef12d42c82aefcd7b.mp4" type='video/mp4'/>  
	    <%-- <source src="${contextPath}/static/test.avi" type='video/avi'/> --%>  
	</video>
	<!-- <iframe src='https://view.officeapps.live.com/op/view.aspx?src=http://47.112.221.30:8080/environmental_protection/upload/productionPlan/f744bdd6e1954f86ba143f4591f80d7d.xls' width='100%' height='100%' frameborder='1'></iframe>
     -->
</body>
<script type="text/javascript">
	var options = {
		height : "900px",
		width : "800px",
		pdfOpenParams : {
			view : 'FitV',
			page : '0'
		},
		name : "mans",
		fallbackLink : "<p>您的浏览器暂不支持此pdf，请下载最新的浏览器</p>"
	};
	var url = 'http://47.112.221.30:8080/environmental_protection/upload/productionPlan/TCPIP.pdf';
	//PDFObject.embed(url, "#example1", options);
	var player = videojs('my_video_1',{
	    muted: true,
		controls : true, 
		autoplay : true,
		height:600, 
		width:800,
		loop : true,
		// 更多配置.....
	});
</script>
</html>
