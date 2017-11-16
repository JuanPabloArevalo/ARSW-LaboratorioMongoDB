var app = (function () {

    var nombreJugador = "NN";
    var userId;
    var ultimoPuntaje;
    var stompClient = null;
    var gameid = 0;
    var puntajeMejores = 100;
    return {
        loadWord: function () {
            gameid = $("#gameid").val();

            $.get("/hangmangames/" + gameid + "/currentword",
                    function (data) {
                        $("#palabra").html("<h1>" + data + "</h1>");
                        app.wsconnect();
                    }
            ).fail(
                    function (data) {
                        alert(data["responseText"]);
                    }
            );
        }
        ,
        wsconnect: function () {
            var socket = new SockJS('/stompendpoint');
            stompClient = Stomp.over(socket);
            stompClient.connect({}, function (frame) {
                console.log('Connected: ' + frame);
                //subscriptions
                stompClient.subscribe('/topic/winner.'+gameid, function (eventbody) {
                    console.info(eventbody);
                    $("#idGanador").text(eventbody.body);
                    $("#idEstado").text("Fin Partida");
                });
                stompClient.subscribe('/topic/wupdate.'+gameid, function (eventbody) {
                    $("#palabra").html("<h1>" + eventbody.body + "</h1>");
                });
            });
        },
        sendLetter: function () {
            var id = gameid;
            var hangmanLetterAttempt = {letter: $("#caracter").val(), username: nombreJugador};
            console.info("Gameid:" + gameid + ",Sending v2:" + JSON.stringify(hangmanLetterAttempt));
            jQuery.ajax({
                url: "/hangmangames/" + id + "/letterattempts",
                type: "POST",
                data: JSON.stringify(hangmanLetterAttempt),
                dataType: "json",
                contentType: "application/json; charset=utf-8"
            }).fail(
                    function (data) {
                        if(""===data["responseText"]){
                            
                        }
                        else{
                            alert(data["responseText"]); 
                        }
                       
                    }
            );
        },
        sendWord: function () {
            var hangmanWordAttempt = {word: $("#adivina").val(), username: nombreJugador};
            var id = gameid;
            jQuery.ajax({
                url: "/hangmangames/" + id + "/wordattempts",
                type: "POST",
                data: JSON.stringify(hangmanWordAttempt),
                dataType: "json",
                contentType: "application/json; charset=utf-8",
                error: function(data) { alert(data["responseText"]); },
            });
        },
        getUserInformation: function () {
            userId = $("#playerid").val();

            $.get("/users/" + userId ,
                    function (data) {
                        $("#idImagenJugador").attr("src",data.photoUrl);
                        nombreJugador = data.name;
                        $("#idNombreJugador").html("<h1>" + nombreJugador + "</h1>");
                        ultimoPuntaje = data.scores[0].valorPuntaje;
                        $("#idUltimoPuntaje").html("<h4> Puntaje más reciente:" + ultimoPuntaje + "</h4>");
                    }
            ).fail(
                    function (data) {
                        alert(data["responseText"]);
                    }

            );
        },
        getBestUsers: function () {
            $.get("/users/score/" + puntajeMejores ,
                    function (data) {
                        data.map(adicionarFila);
                        $("#idPuntajeMayorA").text(puntajeMejores);
                        console.info(data);
                    }
            ).fail(
                    function (data) {
                        alert(data["responseText"]);
                    }

            );
        }
    };

})();


function adicionarFila(item){
    var markup = "<tr><td>" + item.name + "</td></tr>";
    $("table tbody").append(markup);
}