'use strict';

window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);

    $("#btn-join").click(() => join($("#group-id").val()));
    $("#btn-create").click(() => create($("#group-name").val()));
    $("#btn-send").click(() => sendPersonal($("#msg").val(), $("#user-id").val()));
};

function join(groupId) {
    console.log("join");
    $.post("/chat/join", {groupId: groupId}, function (data) {
        console.log("data is " + data);
        window.location.href = "/group/" + groupId;
    }, "json").fail(function(response) {
        alert("Join failed: " + response.responseText);
    });
}

function create(groupName) {
    console.log("create");
    $.post("/chat/create", {groupName: groupName}, function (data) {
        console.log("data is " + data);
        window.location.href = "/group/" + data;
    }, "json").fail(function(response) {
        alert("Create failed: " + response.responseText);
    });
}

function sendPersonal(msg, targetUserId) {
    if (msg !== "" && targetUserId !== "") {
        $.post("/chat/send", {targetUserId: targetUserId}, function (data) {
            console.log(data);

            webSocket.send(msg);
            $("#msg").val("");

        }, "json").fail(function(response) {
            alert("Target user set failed: " + response.responseText);
        });
    }
}

function updateChatRoom(message) {
    // Parse the JSON string to an object
    var data = message.data;
    // console.log(data);
    var messageObj = JSON.parse(data);

    // Assuming messageObj has a property called 'userMessage' that contains the HTML content
    var userMessage = messageObj.userMessage;

    // Assuming there's an element with id 'chat-area' where messages should be displayed
    var chatArea = document.getElementById('chatArea');

    // Append the new message to the chat area
    // .append() is a jQuery method, so ensure jQuery is included in your HTML.
    // If you're not using jQuery, you can use .innerHTML or .insertAdjacentHTML
    console.log(userMessage);
    $(chatArea).append(userMessage);
}

