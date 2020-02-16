package com.sflpro.chat.service.impl;

import com.sflpro.chat.model.Mail;
import com.sflpro.chat.model.User;
import com.sflpro.chat.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

@Service
public class EmailServiceImpl implements EmailService {
    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void sendEmail(Mail mail, String templateName) throws MessagingException {
        MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message,
                MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED, StandardCharsets.UTF_8.name());
        Context context = new Context();
        context.setVariables(mail.getModel());
        String html = templateEngine.process(templateName, context);
        helper.setTo(mail.getTo());
        helper.setText(html, true);
        helper.setSubject(mail.getSubject());
        helper.setFrom(mail.getFrom());
        javaMailSender.send(message);
    }

    @Override
    public Mail buildEmailMessageModel(User user, String subject, HashMap<String, Object> map) {
        return new Mail("haykpapikyan6@gmail.com", user.getEmail(), subject, map);
    }
}
