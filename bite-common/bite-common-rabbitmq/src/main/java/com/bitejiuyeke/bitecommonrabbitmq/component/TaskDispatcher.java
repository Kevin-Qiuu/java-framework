package com.bitejiuyeke.bitecommonrabbitmq.component;

import com.bitejiuyeke.bitecommondomain.exception.ServiceException;
import com.bitejiuyeke.bitecommonrabbitmq.domain.annotation.MqTaskType;
import com.bitejiuyeke.bitecommonrabbitmq.handler.TaskHandler;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Data
@Component
public class TaskDispatcher {

    @Autowired
    private ApplicationContext applicationContext;

    private final Map<String, TaskHandler> typeHandlerMap = new HashMap<>();

    @PostConstruct
    public void init() {
        applicationContext.getBeansOfType(TaskHandler.class)
                .entrySet()
                .stream()
                .filter(taskHandlerEntry -> taskHandlerEntry.getValue().getClass().getAnnotation(MqTaskType.class) != null)
                .forEach(taskHandlerEntry ->
                        typeHandlerMap.put(taskHandlerEntry.getValue().getClass().getAnnotation(MqTaskType.class).value(),
                                taskHandlerEntry.getValue()));
    }

    public void dispatch(String typeValue, String payloadJson) {
        TaskHandler taskHandler = typeHandlerMap.get(typeValue);
        if (taskHandler == null) {
            throw new ServiceException("未知任务类型：" + typeValue);
        }
        taskHandler.handleTask(payloadJson);
    }

}
