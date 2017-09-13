package com.lovecws.mumu.rabbitmq.quickstart;

import com.lovecws.mumu.rabbitmq.RabbitMQChannel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 快速rabbitmq入门,接受队列消息
 */
public class RabbitMQQuickStart {

    private static final String QUICKSTART_QUEUE_NAME = "quickstartqueue";

    /**
     * 发送消息
     *
     * @param message 消息内容 不能为空
     */
    public void sendQuickstartMessage(String message) throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        System.out.println(channel);
        AMQP.Queue.DeclareOk declareOk = channel.getChannel().queueDeclare(QUICKSTART_QUEUE_NAME, false, false, false, null);
        channel.getChannel().basicPublish("", QUICKSTART_QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送成功:" + message);
        channel.close();
    }

    /**
     * 接受消息
     */
    public void receiveQuickstartMessage() throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        AMQP.Queue.DeclareOk declareOk = channel.getChannel().queueDeclare(QUICKSTART_QUEUE_NAME, false, false, false, null);
        System.out.println("等待接受队列【" + QUICKSTART_QUEUE_NAME + "】消息");
        //建立一个消费者 监听消息的接受
        Consumer consumer = new DefaultConsumer(channel.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接受消息:" + message);
            }
        };
        channel.getChannel().basicConsume(QUICKSTART_QUEUE_NAME, true, consumer);
        //channel.close();
    }
}
