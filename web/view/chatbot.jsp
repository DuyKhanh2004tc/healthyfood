<%-- 
    Document   : chatbot
    Created on : Jul 9, 2025, 9:16:42 AM
    Author     : ASUS
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>AI Chatbot</title>
        <link href="${pageContext.request.contextPath}/CSS/chatbot.css" rel="stylesheet" type="text/css"/>
    </head>
    <body>
        <div class="chatwindow">
            <button class="close">❌</button>
            <div class="chat">
                <div class="model">
                    <p>Welcome, do you need any support?</p>
                </div >
                
            </div>
            <div class="input-area">
                <input type="text" placeholder="Ask me something..."><button><img src="${pageContext.request.contextPath}/icons/send_icon.png" alt="alt"/></button>
            </div>
        </div>
        
            <script type="importmap">{
              "import" : {
                  "@google/genai": "./node_modules/@google/genai"
              }  
            }
            </script>

        <script type="module" src="${pageContext.request.contextPath}/js/chatbot.js"></script>
    </body>
</html>
