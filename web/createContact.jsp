<%@ page language="java" contentType="text/html; charset=utf-8"%>

<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <title>Новый контакт</title>
    <style>
        <%@include file="common.css"%>
    </style>
</head>
<body>
<div class="logicGroup">
    <%@include file="menu.html"%>
</div>
<div class="logicGroup">
    <form action="/create" method="POST" accept-charset="UTF-8">
        <label for="newName">Имя нового контакта</label>
        <br/>
        <input type="text" id="newName" placeholder="Введите имя"
               name="name" size="30"/>
        <br/>

        <label for="newAddress">Адрес нового контакта</label>
        <br/>
        <input type="text" id="newAddress" placeholder="Введите адрес" name="address" size="30"/>
        <br/>

        <label for="newDecription">Описание номера</label>
        <br/>
        <input type="text" id="newDecription" placeholder="Введите описание к номеру" name="description" size="30"/>
        <br/>

        <label for="newNumber">Номер телефона</label>
        <br/>
        <input type="text" id="newNumber" placeholder="Введите номер телефона"
               parttern="[0-9]+" required name="number" size="30"/>
        <br/>

        <input type="submit" value="Сохранить">
    </form>
</div>
</body>
</html>