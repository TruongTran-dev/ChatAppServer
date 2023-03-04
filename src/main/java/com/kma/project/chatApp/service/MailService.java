package com.kma.project.chatApp.service;

import javax.mail.MessagingException;
import java.io.UnsupportedEncodingException;

public interface MailService {

    void sendMail(String toEmail) throws MessagingException, UnsupportedEncodingException;
}
