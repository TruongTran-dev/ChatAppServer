package com.kma.project.chatapp.service;

public interface NotificationService {

    void sendNotification(String deviceToken, String title, String message);
}
