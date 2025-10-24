package com.bitejiuyeke.biteadminservice.user.config;

import org.springframework.amqp.core.FanoutExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqUserConfig {

    public static final String EXCHANGE_NAME = "edit_user_exchange";

    /**
     * FanoutExchange 是一个扇出交换机 <br>
     * 路由机制：将消息广播到所有绑定到该交换机的队列 <br>
     * 路由键：忽略路由键（routing key），发送消息时不需要指定或路由键不起作用 <br>
     * 使用场景：适用于需要将消息同时发送给多个消费者的场景，如广播通知、系统事件通知等 <br><br>
     *
     * DirectExchange（直连交换机）<br>
     * 路由机制：根据路由键精确匹配，只将消息发送到路由键完全匹配的队列<br>
     * 路由键：必须指定路由键，且需要与队列绑定时的路由键完全一致<br>
     * 使用场景：适用于点对点通信，需要精确控制消息发送到特定队列的场景<br>
     *
     * @return FanoutExchange
     */
    @Bean
    public FanoutExchange editUserExchange() {
        return new FanoutExchange(EXCHANGE_NAME);
    }

}
