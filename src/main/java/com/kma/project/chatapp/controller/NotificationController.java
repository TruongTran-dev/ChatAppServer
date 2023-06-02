package com.kma.project.chatapp.controller;

import com.kma.project.chatapp.dto.request.auth.PushNotificationRequest;
import com.kma.project.chatapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api/v1/notifications")
@RestController
public class NotificationController {

    @Autowired
    NotificationService notificationService;

    @PostMapping("/send-notification")
    public void sendNotification(@RequestBody PushNotificationRequest request) {
        String deviceToken = request.getToken();
        String title = request.getTitle();
        String message = request.getMessage();

        notificationService.sendNotification(deviceToken, title, message);
    }

}
