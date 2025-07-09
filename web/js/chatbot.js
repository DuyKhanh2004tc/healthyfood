/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//xu ly event
const Gemini_Key = "AIzaSyBloWReI20LRfYXQYQh0gZ5k3SgVYRFDOw";
document.querySelector(".input-area button").addEventListener("click", sendMessage);
const conversations = [
//     {
//        "role": "user",
//        "parts": [
//          {
//            "text": "Hello"
//          }
//        ]
//      },
//      {
//        "role": "model",
//        "parts": [
//          {
//            "text": "Great to meet you. What would you like to know?"
//          }
//        ]
//      },
//      {
//        "role": "user",
//        "parts": [
//          {
//            "text": "I have two dogs in my house. How many paws are in my house?"
//          }
//        ]
//      }
];
function sendMessage() {
//    alert("Hi");
    //lay du lieu trong input
    const userMessage = document.querySelector(".input-area input").value;
    // neu co nhap du lieu thi moi dua data len .chat
    if (userMessage.length) {
        // xoa du lieu trong input
        document.querySelector(".input-area input").value = "";
        document.querySelector(".chatwindow .chat").insertAdjacentHTML("beforeend", `<div class="user">
                    <p>${userMessage}</p>
                </div >`);
        //them vao conversation
        conversations.push({
        "role": "user",
        "parts": [
          {
            "text": userMessage,
          }
        ]
      });
        // gui du lieu cho model va tra ve ket qua
        sendData();
    }

    async function sendData() {
        const response = await fetch("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + Gemini_Key, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            
            body: JSON.stringify({"contents": conversations,
                    
                }),
            // …
        });
        const data = await response.json();
        console.log(data);
        //in du lieu vao .chat
        document.querySelector(".chatwindow .chat").insertAdjacentHTML("beforeend", `<div class="model">
                    <p>${data.candidates[0].content.parts[0].text}</p>
                </div >`);
        //them du lieu tra ve cua model vao conversations
        conversations.push({
        "role": "model",
        "parts": [
          {
            "text": data.candidates[0].content.parts[0].text,
          }
        ]
      })
    }
}