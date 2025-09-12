package com.bitejiuyeke.bitecommonrabbitmq.config;

import jakarta.annotation.PostConstruct;
import lombok.Data;
import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration 会被 Spring 使用 CGLIB 代理
 * 保证这里面方法的每一个 Bean 不会被重复创建
 */

@Configuration
@Data
public class RabbitMqConfig {

    /**
     * 队列名称
     */
    @Value("${rabbitmq-component.queue-name:DirectQueue}")
    private String queueName;
    /**
     * 交换机名称
     */
    @Value("${rabbitmq-component.exchange-name:DirectExchange}")
    private String exchangeName;
    /**
     * 路由 key
     */
    @Value("${rabbitmq-component.routing:DirectRouting}")
    private String routing;

    /**
     * 死信队列名称
     */
    @Value("${rabbitmq-component.dlx-queue-name:DlxDirectQueue}")
    private String dlxQueueName;
    /**
     * 死信交换机名称
     */
    @Value("${rabbitmq-component.dlx-exchange-name:DlxDirectExchange}")
    private String dlxExchangeName;
    /**
     * 死信路由 key
     */
    @Value("${rabbitmq-component.dlx-routing:DlxDirectRouting}")
    private String dlxRouting;

    @Bean
    public Queue testQueue() {
        return QueueBuilder.durable("testQueue").build();
    }

   /**
     * 队列 起名：DirectQueue
     *
     */
    @Bean
    public Queue directQueue() {
        // durable:是否持久化,默认是false,持久化队列：会被存储在磁盘上，当消息代理重启时仍然存在，暂存队列：当前连接有效
        // exclusive:默认也是false，只能被当前创建的连接使用，而且当连接关闭后队列即被删除。此参考优先级高于durable
        // autoDelete:是否自动删除，当没有生产者或者消费者使用此队列，该队列会自动删除。
        // return new Queue("DirectQueue",true,true,false);

        // 一般设置一下队列的持久化就好,其余两个就是默认false
        // return new Queue(QUEUE_NAME,true);

        // 普通队列绑定死信交换机
        return QueueBuilder.durable(queueName)
                .deadLetterExchange(dlxExchangeName)
                .deadLetterRoutingKey(dlxRouting).build();
    }

    /**
     * Direct交换机 起名：DirectExchange
     *
     * @return
     */
    @Bean
    DirectExchange directExchange() {
        return new DirectExchange(exchangeName,true,false);
    }

    /**
     * 绑定  将队列和交换机绑定, 并设置用于匹配键：DirectRouting
     *
     * @return
     */
    @Bean
    Binding bindingDirect() {
        return BindingBuilder.bind(directQueue())
                .to(directExchange())
                .with(routing);
    }

    /**
     * 死信队列
     *
     * @return
     */
    @Bean
    public Queue dlxQueue() {


        // 一般设置一下队列的持久化就好,其余两个就是默认false
        return new Queue(dlxExchangeName,true);
    }

    /**
     * 死信交换机
     *
     * @return
     */
    @Bean
    DirectExchange dlxExchange() {
        return new DirectExchange(dlxExchangeName,true,false);
    }

    /**
     * 绑定死信队列与交换机
     *
     * @return
     */
    @Bean
    Binding bindingDlx() {
        return BindingBuilder.bind(dlxQueue())
                .to(dlxExchange())
                .with(dlxRouting);
    }


    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

}
