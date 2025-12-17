package com.ayush.nursery.serviceImpl;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class MailingService {

    private final JavaMailSender mailSender;

    @Autowired
    public MailingService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async("mailTaskExecutor")
    public void sendMail(String to, String subject, String body, File attachment) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        if (to != null && !to.isEmpty()) {
            helper.setTo(to);
        } else {
            throw new IllegalArgumentException("The 'to' field cannot be null or empty.");
        }

        helper.setSubject(subject);
        helper.setText(body, false);
        helper.setFrom("Backup System <ayushnurserybackup@gmail.com>");

        if (attachment != null) {
            helper.addAttachment(attachment.getName(), attachment);
        }

        mailSender.send(message);
    }

    public String createMailTemplate(String date, String time) {
        return String.format("Backup System\n\nDate: %s\nTime: %s\n\nPlease find the backup file attached.", date, time);
    }
}