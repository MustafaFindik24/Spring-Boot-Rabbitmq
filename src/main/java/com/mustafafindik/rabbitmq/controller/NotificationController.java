package com.mustafafindik.rabbitmq.controller;

import com.mustafafindik.rabbitmq.model.Notification;
import com.mustafafindik.rabbitmq.producer.NotificationProducer;
import com.mustafafindik.rabbitmq.consumer.NotificationListener;
import com.mustafafindik.rabbitmq.service.NotificationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping({"/notification"})
public class NotificationController {
    @Value("${springrabbitmq.rabbit.routing.name}")
    private String routingName;
    @Value("${springrabbitmq.rabbit.exchange.name}")
    private String exchangeName;
    private final RabbitTemplate rabbitTemplate;
    private final NotificationProducer notificationProducer;
    private final NotificationService notificationService;
    private final NotificationListener notificationListener;

    public NotificationController(RabbitTemplate rabbitTemplate, NotificationProducer notificationProducer, NotificationService notificationService, NotificationListener notificationListener) {
        this.rabbitTemplate = rabbitTemplate;
        this.notificationProducer = notificationProducer;
        this.notificationService = notificationService;
        this.notificationListener = notificationListener;
    }
    @PostMapping
    public void sendToQueueAndSave(@RequestBody Notification notification) {
        notificationProducer.sendToQueue(notification);
    }
}
