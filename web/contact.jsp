<%@ page language="java" contentType="text/html; ISO-8859-1" pageEncoding="UTF-16" %>

<%@ page import="src.phonebook.contact.Contact"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%--<c:set value="cont" property="contact"/>--%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>${contact.name}</title>
    <style>
        <%@ include file="common.css"%>
    </style>
</head>

<body>

<%@ include file="menu.html"%>

<div>
    <h3>${contact.name}</h3>
    <p>Адрес: ${contact.address}</p>
    <p>Дата создания: ${String.format("%tF", contact.uploadDate)}</p>
    <ul>
        <c:forEach var="phone" items="${contact.numbers}">
            <li>
                <p><c:if test="${phone.description != null && !phone.description.equals(\"\")}">
                    ${phone.description} :
                </c:if> ${phone.number}</p>
            </li>
        </c:forEach>
    </ul>
</div>

</body>
</html>
