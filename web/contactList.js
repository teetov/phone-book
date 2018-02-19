var displayForm = false;

function deleteContacts() {
    switchFormDisplay();
    if(!displayForm) {
        var param = collectDeletedContacts();
        //Если нет контактов под удаление - ничего не делать.
        if(param == "idSet=") {
        } else {
            param = "target=remove&" + param;
            sendPostAndReloadPage(param);
        }
    }
}

//
function switchFormDisplay() {
    var forms = document.getElementsByClassName("hiddenForm");
    if(!displayForm) {
        document.getElementById("deleteButton").innerText = "Удалить выбранные контакты";
        for(var i = 0; i < forms.length; i++) {
            forms[i].style.display = "block";
        }
        displayForm = true;
    } else {
        document.getElementById("deleteButton").innerText = "Выбрать котакты для удаления";
        for(var i = 0; i < forms.length; i++) {
            forms[i].style.display = "none";
        }
        displayForm = false;
    }
}

// Функция собирает id контактов, выбранных для удаления, и формирует из них запрос.
function collectDeletedContacts() {
    var res = "idSet=";

    var boxes = document.getElementsByName("deleteCheckbox");

    for(var i = 0; i < boxes.length; i++) {
        if(boxes[i].checked) {
            res += "#" + boxes[i].value;
        }
    }
    return res;
}

function  sendPostAndReloadPage(param) {
    var xhttp = new XMLHttpRequest();

    var url = "/contacts";

    xhttp.open("POST", url, true);
    xhttp.onreadystatechange = function () {
        if(this.readyState == 4 && this.status == 200) {
            window.location.reload();
        }
    }
    xhttp.setRequestHeader("Content-type", "application/x-www-form-urlencoded; charset=utf-8");

    xhttp.send(param);
}