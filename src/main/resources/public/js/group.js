'use strict';
/**
 * Entry point into chat room
 */
window.onload = function() {

    webSocket.onclose = () => alert("WebSocket connection closed");
    webSocket.onmessage = (msg) => updateChatRoom(msg);

    $("#btn-msg").click(() => sendMessage($("#message").val(), $("#msg-type").val()));

    $("#btn-leave").click(() => leave($("#btn-leave").val()));

    const msgGeneral = $("#msg-general");
    msgGeneral.click(() => changeType(msgGeneral.val()));

    const msgAnonymous = $("#msg-anonymous");
    msgAnonymous.click(() => changeType(msgAnonymous.val()));

    const msgEmphasis = $("#msg-emphasis");
    msgEmphasis.click(() => changeType(msgEmphasis.val()));

    const msgImportant = $("#msg-important");
    msgImportant.click(() => changeType(msgImportant.val()));

};

function leave(groupId) {
    console.log("leave");
    $.post("/chat/leave", {groupId: groupId}, function (data) {
        console.log("data is " + data);
        window.location.href = "/chat";
    }, "json");
}

function changeType(type) {
        console.log(type);
        $.post("/group/type", {type: type}, function (data) {
            console.log("data is " + data);
            // window.location.href = "/group/" + groupId;
        }, "json");
}
function sendMessage(msg) {
    if (msg !== "") {
        webSocket.send(msg);
        $("#message").val("");
    }
}

/**
 * Update the chat room with a message.
 * @param message  The message to update the chat room with.
 */
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

