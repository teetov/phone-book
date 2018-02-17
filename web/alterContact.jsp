<%@page language="java" contentType="text/html; charset=utf-8"%>

<%@ page import="src.phonebook.contact.Contact"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>${contact.name}</title>
    <style>
        <%@ include file="common.css"%>
    </style>
    <script>
        var contactId = ${contact.id};

        <%@ include file="alterContact.js"%>
    </script>
</head>

<body>

<%@ include file="menu.html"%>

<div>
    <form onsubmit="changeContactParameters()" action="" id="contactForm" >
        <label for="newName">Имя контакта</label>
        <br/>
        <input type="text" placeholder="Введите новое имя" value="${contact.name}" id="newName"/>
        <br/>
        <label for="newAddress">Адрес контакта</label>
        <br/>
        <input type="text" placeholder="Введите новый адрес" value="${contact.address}" id="newAddress"/>
        <br/>
    </form>

    <form id="phoneForm">
        <c:forEach var="phone" items="${contact.numbers}">
            <div class="fieldWrapper" id="${phone.id}">
                    <label for="description${phone.id}">Описание</label>
                    <input type="text" value="${phone.description}" placeholder="Введите новое описание" id="description${phone.id}"
                    class="descriptionInput"/>
                    <br>

                    <label for="number${phone.id}">Номер телефона</label>
                    <input type="text" value="${phone.number}" placeholder="Введите новоый номер" id="number${phone.id}"
                    class="numberInput"/>
                    <br>

                    <label for=radio${phone.id}">Выберете номер поумолчанию</label>
                    <input type="radio" name="default" value="${phone.id}" id="radio${phone.id}"
                        <c:if test="${contact.defaultNumber == phone}">
                            checked
                        </c:if>
                    />
                    <br/>

                    <label for=delete${phone.id}">Отметить для удаления</label>
                    <input type="checkbox" name="checkForDelete" value="${phone.id}" id="delete${phone.id}"/>
                    <br>

                    <br>
            </div>
        </c:forEach>
    </form>

    <button onclick="sendParameters()">Сохранить</button >
    <button onclick="deleteNumbers()">Удалить отмеченные номера</button >

    <form>
        <p>Добавить новый котакт</p>
        <label for="newDescr">Описание </label>
        <input type="text" id="newDescr" placeholder="Введите новое описание"/>
        <label for="newNumber">Номер телефона</label>
        <input type="text" id="newNumber" placeholder="Введите новый номер"/>
    </form>
    <button onclick="addNewNumber()">Добваить</button>

</div>
</body>
</html>