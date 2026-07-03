package com.example.rabbitmqdemo.web;

import com.example.rabbitmqdemo.message.CourseNoticeMessage;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mq/demo")
public class RabbitDemoController {

    private final RabbitTemplate rabbitTemplate;

    @Value("${demo.rabbitmq.exchange}")
    private String exchangeName;

    @Value("${demo.rabbitmq.routing-key}")
    private String routingKey;

    public RabbitDemoController(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @GetMapping("/ping")
    public Map<String, Object> ping() {
        return Map.of(
            "status", "UP",
            "message", "RabbitMQ demo application is running.",
            "sendMessageApi", "POST /mq/demo/notice?studentId=10001&teachingClassId=20001"
        );
    }

    @PostMapping("/notice")
    public Map<String, Object> sendNotice(
        @RequestParam(defaultValue = "10001") Long studentId,
        @RequestParam(defaultValue = "20001") Long teachingClassId
    ) {
        CourseNoticeMessage message = new CourseNoticeMessage();
        message.setRequestId("REQ-" + UUID.randomUUID());
        message.setStudentId(studentId);
        message.setTeachingClassId(teachingClassId);
        message.setContent("Course selection request accepted.");
        message.setCreatedAt(LocalDateTime.now());

        rabbitTemplate.convertAndSend(exchangeName, routingKey, message);

        return Map.of(
            "status", "SENT",
            "exchange", exchangeName,
            "routingKey", routingKey,
            "message", message
        );
    }
}
