<%@ page language="java" contentType="text/html; charset=utf-8"%>

<%@ page import="src.phonebook.contact.Contact"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>${contact.name}</title>
    <style>
        <%@ include file="common.css"%>
    </style>
</head>

<body>
<div class="logicGroup">
    <%@ include file="menu.html"%>
</div>
<div class="logicGroup">


    <h3 id="name">${contact.name}</h3>
    <a href="/alter?id=${contact.id}">Редактировать</a>
    <p id="address">
        Адрес: ${contact.address}
        <c:if test="${contact.address == null || contact.address.equals(\"\")}">
            не указан
        </c:if>
    </p>

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
