$(document).ready(function() {
    $("#calculateUIButton").click(function() {
        let operator = $("#operator").val();
        let nums = $("#nums").val();
        let requestData = {"operator": operator, "nums": nums};

        $.ajax({
            type: "POST",
            url: "/calculateUIResult",
            contentType: "application/json",
            data: JSON.stringify(requestData),
            success: function(response) {
                $("#result").val(response);
            },
            error: function(xhr, status, error) {
                $("#result").val("ERROR");
                console.log("Error: " + error);
            }
        });
    });
});

$(document).ready(function() {
    $("#nums").keypress(function(e) {
        if (e.which === 13) { // Проверка нажатия клавиши "Enter"
            e.preventDefault(); // Предотвращаем стандартное поведение формы
            let inputString = $("#nums").val(); // Получаем введенную строку
            sendConsoleResult(inputString); // Отправляем запрос на сервер
        }
    });

    function sendConsoleResult(inputString) {
        $.ajax({
            type: "POST",
            url: "/calculateConsoleResult",
            contentType: "text/plain",
            data: inputString,
            success: function(response) {
                $("#result").val(response);
                $("#nums").val(''); // Очищаем поле "nums"
            },
            error: function(xhr, status, error) {
                $("#result").val("ERROR");
                console.log("Error: " + error);
            }
        });
    }
});