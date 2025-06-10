/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import java.util.Properties;

/**
 *
 * @author Hoa
 */
public class Mail {
    
      public static void sendMail(String toEmail, String subject, String content) {
        // Thông tin tài khoản email gửi đi (bạn cần thay bằng email thật)
        final String fromEmail = "healthyfoodshopteam5se1905@gmail.com"; // email của bạn
        final String password = "oubk vopl ikrz lavi"; // mật khẩu hoặc app password

        // Cấu hình SMTP server của Gmail
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Gmail
        props.put("mail.smtp.port", "587"); // Port TLS
        props.put("mail.smtp.auth", "true"); // Yêu cầu xác thực
        props.put("mail.smtp.starttls.enable", "true"); // Bật STARTTLS

        // Tạo session với xác thực
        Session session = Session.getInstance(props, new jakarta.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            // Tạo email message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress(fromEmail));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            msg.setSubject(subject);
            msg.setText(content);
            msg.setSentDate(new java.util.Date());

            // Gửi email
            Transport.send(msg);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}