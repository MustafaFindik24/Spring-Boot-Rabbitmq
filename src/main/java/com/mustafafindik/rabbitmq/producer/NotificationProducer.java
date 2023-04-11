package com.mustafafindik.rabbitmq.producer;

import com.mustafafindik.rabbitmq.model.Notification;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class NotificationProducer {
    @Value("${springrabbitmq.rabbit.routing.name}")
    private String routingName;
    @Value("${springrabbitmq.rabbit.exchange.name}")
    private String exchangeName;
    private final RabbitTemplate rabbitTemplate;
    public NotificationProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    public void sendToQueue(Notification notification) {
        log.info("\nNotification Send ID : " + UUID.randomUUID());
        this.rabbitTemplate.convertAndSend(this.exchangeName, this.routingName, notification);
    }
}
