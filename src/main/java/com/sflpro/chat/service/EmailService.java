package com.sflpro.chat.service;

import com.sflpro.chat.model.Mail;
import com.sflpro.chat.model.User;

import javax.mail.MessagingException;
import java.util.HashMap;

public interface EmailService {
    void sendEmail(Mail mail, String templateName) throws MessagingException;

    Mail buildEmailMessageModel(User user, String subject,
                                HashMap<String, Object> map);
}
