package com.example.itsapp.util

import java.util.*
import javax.mail.Message
import javax.mail.PasswordAuthentication
import javax.mail.Session
import javax.mail.Transport
import javax.mail.internet.InternetAddress
import javax.mail.internet.MimeMessage

class MailSender{

    private var emailCode = ""
    init {
        emailCode = createEmailCode()
    }

    fun sendEmail(
        title: String,      // 메일 제목
        body: String,       // 메일 내용
        dest: String       // 받는 메일 주소
    ){
        // 보내는 메일 주소와 비밀번호
        val username = "jgh6272@gmail.com";
        val password = "Hyung0725@";

        val props = Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // 비밀번호 인증으로 세션 생성
        val session = Session.getInstance(props,
            object: javax.mail.Authenticator() {
                override  fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(username, password);
                }
            })

        // 메시지 객체 만들기
        val message = MimeMessage(session)
        message.setFrom(InternetAddress(username))
        // 수신자 설정, 여러명으로도 가능
        message.setRecipients(
            Message.RecipientType.TO,
            InternetAddress.parse(dest))
        message.subject = title
        message.setText(body)

        // 전송
        Transport.send(message)
    }
    fun getEmailCode():String{
        return emailCode
    }
    private fun createEmailCode():String{
        //이메일 인증코드 생성
        val str = arrayOf(
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m",
            "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z",
            "1", "2", "3", "4", "5", "6", "7", "8", "9"
        )
        var newCode = String()

        for (x in 0..7) {
            val random = (Math.random() * str.size).toInt()
            newCode += str[random]
        }

        return newCode
    }
}