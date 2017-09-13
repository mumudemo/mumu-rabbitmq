package com.lovecws.mumu.rabbitmq.workqueue;

import com.lovecws.mumu.rabbitmq.RabbitMQChannel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 发送 工作队列，多人可共享该队列，每个人都可以从队列中获取消息，并且每个消息只能有一个消费者消费
 * 测试证明 当多个消费者订阅同一个队列的时候 平均消费消息
 */
public class RabbitMQWorkQueue {

    private static final String WORK_QUEUE_NAME = "workqueue";

    /**
     * 发送消息
     *
     * @param message 消息内容 不能为空
     */
    public void sendWorkQueueMessage(String message) throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        System.out.println(channel);
        channel.getChannel().queueDeclare(WORK_QUEUE_NAME, true, false, false, null);
        channel.getChannel().basicPublish("", WORK_QUEUE_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
        System.out.println("发送workQueue消息:" + message);
        channel.close();
    }

    /**
     * 接受工作队列消息
     */
    public void receiveWorkQueueMessage() throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        channel.getChannel().queueDeclare(WORK_QUEUE_NAME, true, false, false, null);
        channel.getChannel().basicQos(1);
        System.out.println("等待接受workQueue队列【"+WORK_QUEUE_NAME+"】消息");
        //建立一个消费者 监听消息的接受
        DefaultConsumer consumer = new DefaultConsumer(channel.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接受workQueue消息:" + message);
                try {
                    for (char ch : message.toCharArray()) {
                        if (ch == '.') {
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException _ignored) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    }
                } finally {
                    getChannel().basicAck(envelope.getDeliveryTag(), false);
                }
            }
        };
        channel.getChannel().basicConsume(WORK_QUEUE_NAME, false, consumer);
    }
}
