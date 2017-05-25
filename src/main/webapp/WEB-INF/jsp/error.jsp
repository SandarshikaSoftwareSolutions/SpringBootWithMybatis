<%--
  Created by IntelliJ IDEA.
  User: ravikiran_gorthi
  Date: 5/18/17
  Time: 10:16 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
    <title>springmybatisdemo</title>
    <link href="<c:url value="/css/style.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/js/jquery-3.2.1.min.js" />"></script>
    <link href="<c:url value="/css/jquery-ui.1.12.1.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.1.12.1.js" />"></script>
</head>
<body>
<div id="header" align="center">
    <h1>Spring Boot Web JSP Example</h1>
</div>
<div id="main" align="center">
        <span>
            <h2>Error Occurred !</h2>
            <c:out value="${exception}"/>
        </span>
</div>
</body>
</html>
