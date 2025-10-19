package com.bitejiuyeke.bitecommonrabbitmq.component;

import com.bitejiuyeke.bitecommonrabbitmq.domain.constants.TaskInfoConstant;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RabbitListener(queues = "${rabbitmq-component.queue-name:DirectQueue}")
public class TaskConsumer {

    @Autowired
    private TaskDispatcher taskDispatcher;

    @RabbitHandler
    public void process(Map<String, String> taskMsg) {
        String taskType = taskMsg.get(TaskInfoConstant.TASK_TYPE);
        String payload = taskMsg.get(TaskInfoConstant.PAYLOAD);
        taskDispatcher.dispatch(taskType, payload);
    }

}
