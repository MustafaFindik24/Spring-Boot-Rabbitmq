package com.mustafafindik.rabbitmq.consumer;

import com.mustafafindik.rabbitmq.model.Notification;
import com.mustafafindik.rabbitmq.service.NotificationServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Component
@Slf4j
public class NotificationListener {
    private final NotificationServiceImpl notificationService;

    public NotificationListener(NotificationServiceImpl notificationService) {
        this.notificationService = notificationService;
    }
    @RabbitListener(queues = {"${springrabbitmq.rabbit.queue.name}"})
    public void handleMessage(Notification notification) {
        log.info("Notification caught : " + notification.toString());
        notificationService.saveNotify(notification);
    }
}

