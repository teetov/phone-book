<%@ page language="java" contentType="text/html; charset=utf-8" %>

<%@ page import="src.phonebook.contact.PhoneBook" %>
<%@ page import="src.phonebook.contact.Contact" %>
<%@ page import="java.util.List" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <meta http-equiv="Content-Type" content="text/html">
    <title>Списов контактов</title>
    <style>
        <%@include file="common.css"%>
        .hiddenForm {
            display: none;
        }
    </style>
    <script charset="UTF-8">
        <%@ include file="contactList.js"%>
    </script>
</head>
<body>
<div class="logicGroup">
    <%@ include file="menu.html"%>
</div>
<div class="logicGroup">
<button id="deleteButton" onclick="deleteContacts()" >Выбрать контакты для удаления</button>
    <c:forEach var="contact" items="${printBook}">
        <div>
            <h4><a href="/contact?id=${contact.id}">${contact.name}</a></h4>
            <a href="/alter?id=${contact.id}">Редактировать контакт</a>

            <form class="hiddenForm">
                <label for="delete${contact.id}">Удалить этот контакт</label>
                <input type="checkbox" value="${contact.id}" name="deleteCheckbox" id="delete${contact.id}"/>
            </form>

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
</div>
</body>
</html>