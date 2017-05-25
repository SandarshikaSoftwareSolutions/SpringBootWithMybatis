<%--
  Created by IntelliJ IDEA.
  User: ravikiran_gorthi
  Date: 5/4/17
  Time: 7:05 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<html>
<head>
    <title>springmybatisdemo</title>
    <link href="<c:url value="/css/style.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/js/jquery-3.2.1.min.js" />"></script>
    <link href="<c:url value="/css/jquery-ui.1.12.1.css" />" rel="stylesheet">
    <script type="text/javascript" src="<c:url value="/js/jquery-ui.1.12.1.js" />"></script>

    <script>
        $(document).ready(function () {
            //----- OPEN
            $('[data-popup="error"]').fadeIn(350);

            //----- CLOSE
            $('[data-popup-close]').on('click', function (e) {
                var targeted_popup_class = jQuery(this).attr('data-popup-close');
                $('[data-popup="error"]').fadeOut(350);
            });
        });
    </script>
</head>
<body>
<div id="header" align="center">
    <h1>JDBC CRUD Operations Test</h1>
</div>
<div id="main" align="center">
    <c:if test="${mybatisMessage != null}">
        <div class="popup" data-popup="error">
            <div class="popup-inner">
                <h2>Message to You !</h2>
                <br>
                <center><font color="red"><c:out value="${mybatisMessage}"/></font></center>
                <br>
                <a data-popup-close="error" class="button green">Close</a>
                <a style="text-decoration: none;" class="popup-close" data-popup-close="error">x</a>
            </div>
        </div>
    </c:if>
    <c:if test="${student != null}">
        <c:choose>
            <c:when test="${student.percentage == 0}">
                <c:url var="postUrl" value="saveStudent"/>
                <c:set var="buttonName" value="Add New Student"/>
            </c:when>
            <c:otherwise>
                <c:url var="postUrl" value="../updStudent"/>
                <c:set var="buttonName" value="Update Student"/>
            </c:otherwise>
        </c:choose>
        <form:form modelAttribute="student" action="${postUrl}" method="post">
            <table align="center" id="studentTable">
                <form:hidden path="studentId"/>
                <tr>
                    <th>Student Name :</th>
                    <td><form:input path="name"/><form:errors path="name"/></td>
                </tr>
                <tr>
                    <th>Email Id :</th>
                    <td><form:input path="emailId"/><form:errors path="emailId"/></td>
                </tr>
                <tr>
                    <th>Mobile No. :</th>
                    <td><form:input path="mobileNo"/><form:errors path="mobileNo"/></td>
                </tr>
                <tr>
                    <th>Branch :</th>
                    <td><form:input path="branch"/><form:errors path="branch"/></td>
                </tr>
                <tr>
                    <th>Percentage :</th>
                    <td><form:input path="percentage"/><form:errors path="percentage"/></td>
                </tr>
                <tr>
                    <td align="center" colspan="2">
                        <input class="button shadow animate blue" type="submit" value="${buttonName}"/>
                    </td>
                </tr>
            </table>
        </form:form>
    </c:if>
    <center><h3>Students List</h3></center>
    <c:choose>
        <c:when test="${!empty studentsList}">
            <table align="center" id="studentTable" border="1">
                <th style="width:5%">Sl.No</th>
                <th style="width:20%">Name</th>
                <th style="width:15%">Branch</th>
                <th style="width:5%">Percentage</th>
                <th style="width:15%">Mobile No.</th>
                <th style="width:20%">Email Id</th>
                <th style="width:15%">Edit/Delete</th>

                <c:forEach var="student" items="${studentsList}" varStatus="count">
                    <tr>
                        <td align="center">${count.index + 1}</td>
                        <td>${student.name}</td>
                        <td>${student.branch}</td>
                        <td align="center">${student.percentage}</td>
                        <td>${student.mobileNo}</td>
                        <td>${student.emailId}</td>
                        <td>
                            <a class="push_button violet" href="getStudent/${student.studentId}">Edit</a>
                            <a class="push_button red" href="deleteStudent/${student.studentId}">Delete</a>
                        </td>
                    </tr>
                </c:forEach>
            </table>
        </c:when>
        <c:otherwise>
            <table align="center" id="studentTable">
                <tr>
                    <td align="center" colspan="5"><h4>No Students were found in the Database</h4></td>
                </tr>
            </table>
        </c:otherwise>
    </c:choose>
</div>
</body>
</html>