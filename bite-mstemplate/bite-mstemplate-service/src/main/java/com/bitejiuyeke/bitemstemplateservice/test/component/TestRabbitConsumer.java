package com.bitejiuyeke.bitemstemplateservice.test.component;

import com.bitejiuyeke.bitecommonrabbitmq.config.RabbitMqConfig;
import com.bitejiuyeke.bitemstemplateservice.domain.MessageDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RabbitListener(queues = "${rabbitmq-component.queue-name:DirectQueue}")
public class TestRabbitConsumer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @RabbitHandler
    public void processMsg(MessageDTO messageDTO) {
        log.info("已消费一个消息：{}", messageDTO);
    }


}
