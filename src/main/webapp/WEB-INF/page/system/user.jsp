<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"></c:set>
<!DOCTYPE html>
<html>
<head>
<title>user</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0">
<meta http-equiv="X-UA-Compatible" content="ie=edge">
<%-- <link rel="stylesheet" href="${contextPath}/static/layui/css/layui.css" /> --%>
<script src="${contextPath}/static/layui/layui.js"></script>
<script type="text/javascript" src="${contextPath}/static/js/jquery-1.6.js"></script>
</head>
<body>
    <table>
        <thead>
            <tr><th>ID</th><th>用户名</th><th>密码</th><th>昵称</th></tr>
        </thead>
        <tbody>
            <c:forEach items="${page.rows}" var="user">
            <tr><td>${user.userId}</td><td>${user.userName}</td><td>${user.password}</td><td>${user.nickName}</td></tr>
            </c:forEach>
        </tbody>
    </table>
    <button onclick="pre();">&lt;&lt;</button>
    <button onclick="next();">>></button>
    <button id="upload">upload</button>
</body>
<script type="text/javascript">
(function(){
    $.ajaxSetup({
	  error: function(xhr,status,err){
		  console.log(xhr);
		  if(xhr.status==401){
			  window.location.href='${contextPath}/login.jsp';
		  }
		  if(xhr.status==301){
			  alert('没有操作权限，请联系管理员');
		  }
	  }
	});
	var pno=1;
	//getPage(pno);
	function getPage(pno){
		$.get('${contextPath}/user/list',{pno:pno,nickName:'管理员'},function(res){
			$('table').empty().html(res);
		});
	}
	window.pre = function(){
		pno>1?pno--:pno=1;
		getPage(pno);
	}
	window.next = function(){
        pno++;
        getPage(pno);
    }
})()
layui.use(['element','form','upload','layer','table','laydate'], function(){
  var $ = layui.jquery, element = layui.element, form = layui.form, upload = layui.upload, layer = layui.layer, table = layui.table, laydate = layui.laydate

  upload.render({
    elem: '#upload',
    url: '${contextPath}/file/upload', //上传接口
    accept: 'file',
    size: 10240, //设置文件最大可允许上传的大小，单位 KB。
    done: function(res){
      if(res.status===1){
        alert(res.msg);
      }
    },
    error: function(){
      return layer.msg('网络异常！');
    }
  });
});
var Sys = {};
var ua = navigator.userAgent.toLowerCase();
if (!!window.ActiveXObject || "ActiveXObject" in window)
    Sys.ie = true;//ua.match(/msie ([\d.]+)/)[1];
else if (document.getBoxObjectFor)
    Sys.firefox = ua.match(/firefox\/([\d.]+)/)[1];
else if (window.MessageEvent && !document.getBoxObjectFor)
    Sys.chrome = ua.match(/chrome\/([\d.]+)/)[1];
else if (window.opera)
    Sys.opera = ua.match(/opera.([\d.]+)/)[1];
else if (window.openDatabase)
    Sys.safari = ua.match(/version\/([\d.]+)/)[1];
console.log(Sys);
</script>
</html>
