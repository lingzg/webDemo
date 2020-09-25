<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>        
        <thead>
            <tr><th>ID</th><th>用户名</th><th>密码</th><th>昵称</th></tr>
        </thead>
        <tbody>
            <c:forEach items="${page.rows}" var="user">
            <tr><td>${user.userId}</td><td>${user.userName}</td><td>${user.password}</td><td>${user.nickName}</td></tr>
            </c:forEach>
        </tbody>