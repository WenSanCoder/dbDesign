package com.example.rabbitmqdemo.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitDemoConfig {

    @Value("${demo.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${demo.rabbitmq.queue}")
    private String queueName;

    @Value("${demo.rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public DirectExchange demoExchange() {
        return new DirectExchange(exchangeName, true, false);
    }

    @Bean
    public Queue demoQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding demoBinding(Queue demoQueue, DirectExchange demoExchange) {
        return BindingBuilder.bind(demoQueue).to(demoExchange).with(routingKey);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
