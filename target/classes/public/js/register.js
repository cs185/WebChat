'use strict';

window.onload = function() {

    $("#button").click(() => register($("#username").val(), $("#name").val(), $("#password").val()));
};

function register(username, name, password) {
    $.post("/register", {username: username, name: name, password: password}, function (data) {
        // Redirect to login page on successful registration
        window.location.href = "/login";
        console.log(data);
    }).fail(function(response) {
        alert("Register failed: " + response.responseText);
    });
}