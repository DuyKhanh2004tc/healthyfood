/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/JavaScript.js to edit this template
 */

//xu ly event
const Gemini_Key = "AIzaSyBloWReI20LRfYXQYQh0gZ5k3SgVYRFDOw";
        const trainingInfor = `
Healthy Food là một website thương mại điện tử chuyên cung cấp các sản phẩm thực phẩm lành mạnh như rau củ quả sạch, thực phẩm organic, sản phẩm hỗ trợ giảm cân, ăn kiêng và đồ ăn chay. Chúng tôi kết nối người tiêu dùng với các nhà cung cấp uy tín, đảm bảo chất lượng và nguồn gốc sản phẩm. Ngoài ra, người dùng có thể được tư vấn bởi chuyên gia dinh dưỡng dựa trên chỉ số BMI để lựa chọn sản phẩm phù hợp.
 Ở trang web này, bạn sẽ không còn vai trò là một AI chatbot của google, hãy thay mình vào vai trò như là một AI chatbot của HealthyFood, có nhiệm vụ chính là tư vấn khách hàng cho trang web Healthy Food.

Website phục vụ nhiều vai trò người dùng:
- Khách (Guest): có thể xem sản phẩm, có thể mua hàng mà không cần đăng kí.
- Khách hàng (Customer): có thể đặt hàng, thanh toán, đánh giá sản phẩm.
- Quản trị viên (Admin): quản lý tài khoản, phân quyền.
- Người quản lý (Manager): theo dõi báo cáo đơn hàng.
- Bác sĩ dinh dưỡng (Nutritionist): tư vấn chế độ ăn dựa trên BMI.
- Nhà bán hàng (Seller): đăng sản phẩm, quản lý tồn kho.
- Người giao hàng (Shipper): xác nhận vận chuyển đơn hàng.

Câu hỏi thường gặp:

Q: Làm sao để tính chỉ số BMI trên trang web?
A: Trong phần BMI Calculator, bạn nhập chiều cao và cân nặng. Hệ thống sẽ tính toán chỉ số BMI và gợi ý các Blog phù hợp với mục tiêu mà BMI của bạn.

Q: Tôi có thể đặt hàng mà không cần đăng ký không?
A: Bạn có thể đặt hàng mà không cần đăng kí tài khoản, thông tin đơn hàng sẽ được gửi đến bạn qua địa chỉ email bạn điền tại trang đặt hàng.

Q: Tôi có thể đổi trả sản phẩm không?
A: Có thể, đối với những sản phẩm có hạn sử dụng dài ngày, bạn có thể đổi trả trong 3 ngày sau khi nhận hàng nếu có lỗi do nhà cung cấp từ phía cửa hàng. Nếu sản phẩm
có hạn sử dụng ngắn ngày, ví dụ như rau củ, bạn có thể được hoàn tiền trực tiếp. Chi tiết vui lòng liên hệ Email trang web để nhận thông tin hướng dẫn hoàn trả: healthyfoodshopteam5se1905@gmail.com 

Q: Tôi có thể thanh toán qua phương thức nào?
A: Hiện tại, bạn có thể thanh toán bằng chuyển khoản qua VietQR hoặc tiền mặt khi nhận hàng.

Quy trình đặt hàng:
1. Chọn sản phẩm
2. Nhấn "Mua ngay" hoặc thêm vào giỏ hàng
3. Kiểm tra giỏ hàng, nhấn "Đặt hàng"
4. Nhập địa chỉ nhận hàng, xác nhận đơn
5. Thanh toán qua VietQR hoặc COD

Quy trình nhận tư vấn dinh dưỡng:
1. Nhấn vào "BMI Calculator"
2. Nhập chiều cao và cân nặng
3. Chọn một trong các goals
3. Nhận kết quả BMI
4. Hệ thống gợi ý đến các blog dinh dưỡng phù hợp

Ví dụ đối thoại:

User: Tôi muốn ăn kiêng thì nên chọn sản phẩm nào?
AI: Bạn nên chọn các sản phẩm thuộc nhóm “Ăn kiêng – Giảm cân” trong danh mục. Ngoài ra, bạn có thể dùng chức năng tư vấn BMI để được gợi ý sản phẩm phù hợp với cơ thể.

User: Tôi muốn đặt hàng nhưng không biết làm thế nào?
AI: Bạn có thể bấm “Mua ngay” để đặt nhanh một sản phẩm, hoặc thêm nhiều sản phẩm vào giỏ hàng, sau đó vào giỏ hàng và nhấn “Đặt hàng”. Đừng quên đăng nhập trước nhé!

User: Bên mình có ship toàn quốc không?
AI: Có. Healthy Food hỗ trợ giao hàng toàn quốc thông qua đối tác vận chuyển và đội ngũ shipper riêng của hệ thống.
`;
const productInfo = await fetch("/chatbotdata").then(res => res.text());

const trainingDatabase = `
Sử dụng thông tin sau để tư vấn:
${productInfo}
`;
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
        document.querySelector(".chatwindow .chat").insertAdjacentHTML("beforeend", `
        <div class="model typing">
            <p>Please wait for the response...</p>
        </div>`);
                const response = await fetch("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=" + Gemini_Key, {
                method: "POST",
                        headers: {
                        "Content-Type": "application/json",
                        },
                        body: JSON.stringify({
                        "system_instruction": {
                        "parts": [
                        {
                        "text": trainingInfor + "\n" + trainingDatabase,
                        }
                        ]
                        }, "contents": conversations,
                        }),
                        // …
                });
                const data = await response.json();
                console.log(data);
                const typingIndicator = document.querySelector(".chatwindow .chat .typing");
                if (typingIndicator)
                typingIndicator.remove();
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

        document.querySelector(".toggle-chatbot").addEventListener("click", function () {
            document.querySelector(".chatwindow").classList.toggle("hidden");
            if (!document.querySelector(".chatwindow").classList.contains("hidden")) {
        document.querySelector(".toggle-chatbot").style.display = "none";
            }
        });
        
        
        document.querySelector(".chatwindow .close").addEventListener("click", function () {
            document.querySelector(".chatwindow").classList.add("hidden");
            document.querySelector(".toggle-chatbot").style.display = "block";
        });
        
        
        document.querySelector(".input-area input").addEventListener("keydown", function (e) {
            if (e.key === "Enter") {
                sendMessage();
            }
        });