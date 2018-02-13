<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-16"%>

<%@ page import="src.phonebook.contact.PhoneBook" %>
<%@ page import="src.phonebook.contact.Contact" %>
<%@ page import="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-16">
    <title>Список контактов</title>
    <style>
        <%@include file="common.css"%>
    </style>
</head>
<body>

<%  response.setCharacterEncoding("UTF-16");%>


<%@ include file="menu.html"%>

<c:forEach var="contact" items="${printBook}">
    <div>
        <h4><a href="/contact?id=${contact.id}">${contact.name}</a></h4>
        <p>
            Всего номеров:( ${contact.numbers.size()} )
        </p>
        <c:if test="${contact.defaultNumber != null}">
            <p>
                номер по умолчанию:
                    ${contact.defaultNumber.number}
            </p>
        </c:if>
    </div>
</c:forEach>

<ol>
    <c:forEach var="pageNumb" step="1" begin="1" end="${maxIndex}">
        <li>
            <a href="/contacts?filter=${param.get("filter")}&index=${pageNumb}">
                К страние ${pageNumb}
            </a>
        </li>
    </c:forEach>
</ol>
</body>
</html>