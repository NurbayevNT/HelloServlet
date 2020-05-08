$(document).ready(function(){
 $.ajax
        ({
            type: "POST",//Метод передачи
            url: 'Servlet',//Название сервлета
            success:function(serverData)//Если запрос удачен
            {
                $("#word-info").html(serverData.serverInfo);
            },
            error: function(e)//Если запрос не удачен
            {
                alert("Error")
            }
        });

});
