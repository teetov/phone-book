<%@page language="java" contentType="text/html; charset=utf-8"%>

<%@ page import="src.phonebook.contact.Contact"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
    <title>Редактирование ${contact.name}</title>
    <style>
        <%@ include file="common.css"%>
    </style>
    <script>
        var contactId = ${contact.id};

        <%@ include file="alterContact.js"%>
    </script>
</head>

<body>
<div class="logicGroup">
    <%@ include file="menu.html"%>
</div>
<div>
    <div class="logicGroup">
        <p>
            <a href="/contact?id=${contact.id}">Перейти к просмотру котакта</a>
        </p>

        <form onsubmit="changeContactParameters()" id="contactForm" >
            <label for="newName">Имя контакта</label>
            <br/>
            <input type="text" placeholder="Введите новое имя" value="${contact.name}" id="newName" pattern=".*" required/>
            <br/>
            <label for="newAddress">Адрес контакта</label>
            <br/>
            <input type="text" placeholder="Введите новый адрес" value="${contact.address}" id="newAddress"/>
            <br/>
        </form>

        <p>Номера телефонов:</p>

        <form id="phoneForm" onsubmit="sendParameters(); return false;">
            <c:forEach var="phone" items="${contact.numbers}">
                <div class="fieldWrapper" id="${phone.id}">
                        <label for="description${phone.id}">Описание</label>
                        <input type="text" value="${phone.description}" placeholder="Введите новое описание"
                               id="description${phone.id}" class="descriptionInput" size="30"/>
                        <br>

                        <label for="number${phone.id}">Номер телефона</label>
                        <input type="text" value="${phone.number}" placeholder="Введите новоый номер" id="number${phone.id}"
                        class="numberInput" parttern="[0-9]+"  size="30" required/>
                        <br>

                        <label for=radio${phone.id}">Выбрать номером поумолчанию</label>
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

            <button id="contactChangeButton">Сохранить</button >

        </form>

        <button onclick="deleteNumbers()">Удалить отмеченные номера</button >
    </div>

    <div class="logicGroup">
        <form action="" onsubmit="addNewNumber(); return false;">
            <p>Добавить новый котакт </p>

            <label for="newDescr">Описание </label>
            <input type="text" id="newDescr" placeholder="Введите новое описание" size="30"/>
            <br/>

            <label for="newNumber">Номер телефона </label>
            <input type="text" id="newNumber" placeholder="Введите новый номер" required size="30"/>
            <br/>

            <button>Добваить</button>
        </form>
    </div>
</div>
</body>
</html>