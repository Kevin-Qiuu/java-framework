package com.bitejiuyeke.bitemstemplateservice.test.component;

import com.bitejiuyeke.bitecommonrabbitmq.config.RabbitMqConfig;
import com.bitejiuyeke.bitemstemplateservice.domain.MessageDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class TestRabbitProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private RabbitMqConfig rabbitMqConfig;

    public void produceMsg(MessageDTO messageDTO) {
        rabbitTemplate.convertAndSend(rabbitMqConfig.getExchangeName(),
                rabbitMqConfig.getRouting(), messageDTO);
    }

    public void produceMsgTestQueue(MessageDTO messageDTO) {
        rabbitTemplate.convertAndSend("testQueue", messageDTO);
    }


}
