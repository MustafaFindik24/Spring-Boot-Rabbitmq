package com.mustafafindik.rabbitmq.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqConfiguration {
    @Value("${springrabbitmq.rabbit.queue.name}")
    private String queueName;
    @Value("${springrabbitmq.rabbit.routing.name}")
    private String routingName;
    @Value("${springrabbitmq.rabbit.exchange.name}")
    private String exchangeName;
    private final RabbitTemplate rabbitTemplate;

    public RabbitmqConfiguration(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }
    @Bean
    public Queue queue() {
        return new Queue(queueName);
    }
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(this.exchangeName);
    }
    @Bean
    Binding binding() {
        return BindingBuilder.bind(this.queue()).to(this.directExchange()).with(this.routingName);
    }
}

