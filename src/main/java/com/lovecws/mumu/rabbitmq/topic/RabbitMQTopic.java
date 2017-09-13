package com.lovecws.mumu.rabbitmq.topic;

import com.lovecws.mumu.rabbitmq.RabbitMQChannel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbit 订阅、发布模式
 */
public class RabbitMQTopic {

    private static final String TOPIC = "topic";

    /**
     * 发送消息
     *
     * @param message 消息内容 不能为空
     */
    public void sendTopicMessage(String message) throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        channel.getChannel().exchangeDeclare(TOPIC, "topic");

        channel.getChannel().basicPublish(TOPIC, "routingKey", null, message.getBytes());
        System.out.println("发送主题消息:" + message);
        channel.close();
    }

    /**
     * 接受主题消息
     */
    public void receiveTopicMessage() throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        channel.getChannel().exchangeDeclare(TOPIC, "topic");
        String queueName = channel.getChannel().queueDeclare().getQueue();
        channel.getChannel().queueBind(queueName, TOPIC, "bindingKey");
        System.out.println("等待接受topic主题【" + TOPIC + "】消息");
        System.out.println("选择队列:" + queueName);
        Consumer consumer = new DefaultConsumer(channel.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接受消息:" + envelope.getRoutingKey() + "':'" + message + "'");
            }
        };
        channel.getChannel().basicConsume(queueName, true, consumer);
    }
}
