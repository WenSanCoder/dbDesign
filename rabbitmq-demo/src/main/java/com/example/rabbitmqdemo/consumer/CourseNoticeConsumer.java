package com.example.rabbitmqdemo.consumer;

import com.example.rabbitmqdemo.message.CourseNoticeMessage;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class CourseNoticeConsumer {

    @RabbitListener(queues = "${demo.rabbitmq.queue}")
    public void handleNotice(CourseNoticeMessage message) {
        System.out.println("Received RabbitMQ message: " + message);
    }
}
