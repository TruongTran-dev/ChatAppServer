package com.kma.project.chatapp.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    void sendMail(String toEmail) throws MessagingException, UnsupportedEncodingException;
}
