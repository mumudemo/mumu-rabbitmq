package com.lovecws.mumu.rabbitmq.pubsub;

import com.lovecws.mumu.rabbitmq.RabbitMQChannel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * rabbit 订阅、发布模式
 * 当多个消费者同时订阅一个队列的时候，当这个生产者发给队列发送消息的时候，这两个消费者会获取到全部的消息。
 */
public class RabbitMQPubsub {

    private static final String EXCHANGE_NAME = "exchange";

    /**
     * 发送消息
     * @param message 消息内容 不能为空
     */
    public void sendPubsubMessage(String message) throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        channel.getChannel().exchangeDeclare(EXCHANGE_NAME, "fanout");

        channel.getChannel().basicPublish(EXCHANGE_NAME, "", null, message.getBytes());
        System.out.println("发送pubsub订阅消息:" + message + "'");
        channel.close();
    }

    /**
     * 接受消息
     */
    public void receivePubsubMessage() throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        channel.getChannel().exchangeDeclare(EXCHANGE_NAME, "fanout");
        String queueName = channel.getChannel().queueDeclare().getQueue();
        channel.getChannel().queueBind(queueName, EXCHANGE_NAME, "");
        System.out.println("等待接受pusub订阅【" + EXCHANGE_NAME + "】消息");
        System.out.println("选择队列:"+queueName);
        Consumer consumer = new DefaultConsumer(channel.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接受消息:" + message + "'");
            }
        };
        channel.getChannel().basicConsume(queueName, true, consumer);
    }
}
