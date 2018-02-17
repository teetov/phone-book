
function collectContactParameters() {
    var name = document.getElementById("newName").value;
    var address = document.getElementById("newAddress").value;
    var result = "name="+ name + "&address=" + address;

    return result;
}

function checkRadio() {
    var result = "";
    var radio = document.getElementsByName("default");
    for(var i = 0; i < radio.length; i++) {
        if(radio[i].type == "radio" && radio[i].checked) {
            return result += "default=" + radio[i].value;
        }
    }
    return result;
}

function collectPhoneParameters() {
    var phoneWrappers = document.getElementsByClassName("fieldWrapper");
    var result = "";

    var idSet = "";

    var i;

    for(i = 0; i < phoneWrappers.length; i++) {
        var wrapper = phoneWrappers[i];
        var idPhone = wrapper.getAttribute("id");
        idSet += "#" + idPhone;

        var descr = wrapper.getElementsByClassName("descriptionInput");
        result += "description" + idPhone + "=" + descr[0].value;

        if(result != "")
            result += "&";

        var number = wrapper.getElementsByClassName("numberInput");
        result += "number" + idPhone + "=" + number[0].value;

        if(result != "")
            result += "&";
    }

    idSet = "idSet=" + idSet;

    result += idSet;

    return result;
}

function  sendPostWithoutResponse(param) {
    var xhttp = new XMLHttpRequest();

    var url = "/alter";

    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

    xhttp.send(param);
}

function sendParameters() {
    var contactParam = collectContactParameters();
    var phoneParam = collectPhoneParameters();
    var def = checkRadio()

    var param = contactParam + "&" + phoneParam + "&" + def;

    param += "&target=alter&contactId=" + contactId;

    sendPostWithoutResponse(param);
}

function addNewNumber() {
    var param = collectNewNumber();

    param = "target=add&contactId=" + contactId + "&" + param;

    var url = "/alter";

    var xhttp = new XMLHttpRequest();

    xhttp.onreadystatechange = function() {
        if(this.readyState == 4 && this.status == 200) {
            constructNewForm(this.responseText);
        }
    }

    xhttp.open("POST", url, true);
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

    xhttp.send(param);
}

function collectNewNumber() {
    var result = "";

    result += "description=" + document.getElementById("newDescr").value;
    result += "&number=" + document.getElementById("newNumber").value;
    return result;
}

function constructNewForm(id) {
    var form = document.getElementById("phoneForm");

    var div = document.createElement("div");
    div.id = id;
    div.setAttribute("class", "fieldWrapper");

    form.appendChild(div);

    var breakLine = document.createElement("br");

    //поле редактирования описания
    var input = document.createElement("input");
    input.type = "text";
    input.placeholder = "Введите новое описание";
    input.id="description" + id;
    input.setAttribute("class", "descriptionInput");
    input.value = document.getElementById("newDescr").value;
    document.getElementById("newDescr").value = "";

    var label = document.createElement("label");
    label.setAttribute("for", input.id);
    label.innerText = "Описание";

    div.appendChild(label);
    div.appendChild(input);
    breakLine = document.createElement("br");
    div.appendChild(breakLine);

    //поле редактирования номера
    input = document.createElement("input");
    input.type = "text";
    input.placeholder = "Введите новоый номер";
    input.id="number" + id;
    input.setAttribute("class", "numberInput");
    input.value = document.getElementById("newNumber").value;
    document.getElementById("newNumber").value = "";

    label = document.createElement("label");
    label.setAttribute("for", input.id);
    label.innerText = "Номер телефона";

    div.appendChild(label);
    div.appendChild(input);
    breakLine = document.createElement("br");
    div.appendChild(breakLine);

    //кнопка выбора номера по умолчанию
    input = document.createElement("input");
    input.type = "radio";
    input.name="default";
    input.value = id;
    input.id = "radio" + id;

    if(form.getElementsByClassName("fieldWrapper").length == 1) {
        var checked = document.createAttribute("checked");
        input.setAttributeNode(checked);
    }

    label = document.createElement("label");
    label.for = input.id;
    label.innerText = "Выберите номер поумолчанию";

    div.appendChild(label);
    div.appendChild(input);
    breakLine = document.createElement("br");
    div.appendChild(breakLine);

    //кнопка для удаления
    input = document.createElement("input");
    input.type = "checkbox";
    input.name = "checkForDelete";
    input.value = id;
    input.id = "delete" + id;

    label = document.createElement("label");
    label.for = input.id;
    label.innerText = "Отметить для удаления";

    div.appendChild(label);
    div.appendChild(input);

    //завершающие разграничители
    breakLine = document.createElement("br");
    div.appendChild(breakLine);
    breakLine = document.createElement("br");
    div.appendChild(breakLine);
}

function deleteNumbers() {
    var param = "target=delete&contactId=" + contactId + "&";
    param += collectDeleteNumbers();
    sendPostWithoutResponse(param);
}

function collectDeleteNumbers() {
    var res = "idSet=";

    var deleteList = document.getElementsByName("checkForDelete");

    for(var i = 0; i < deleteList.length; i++) {
        if(deleteList[i].checked) {
            var id = deleteList[i].value;
            res += "#" + id;
            var wrapper= document.getElementById(id);
            wrapper.remove();
        }
    }
    return res;
}