package com.bitejiuyeke.bitecommonrabbitmq.component;

import com.bitejiuyeke.bitecommoncore.utils.JsonUtil;
import com.bitejiuyeke.bitecommonrabbitmq.domain.constants.TaskInfoConstant;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TaskProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 交换机名称
     */
    @Value("${rabbitmq-component.exchange-name:DirectExchange}")
    private String exchangeName;

    /**
     * 路由 key
     */
    @Value("${rabbitmq-component.routing:DirectRouting}")
    private String routingKey;

    public <T> void sendTaskToMq(String taskType, T payload) {
        Map<String, String> taskMsg = new HashMap<>();
        taskMsg.put(TaskInfoConstant.TASK_TYPE, taskType);
        taskMsg.put(TaskInfoConstant.PAYLOAD, JsonUtil.obj2String(payload));
        rabbitTemplate.convertAndSend(exchangeName, routingKey, taskMsg);
    }

}
