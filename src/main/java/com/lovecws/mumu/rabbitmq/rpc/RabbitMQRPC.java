package com.lovecws.mumu.rabbitmq.rpc;

import com.lovecws.mumu.rabbitmq.RabbitMQChannel;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeoutException;

/**
 * rabbit rpc远程方法调用
 */
public class RabbitMQRPC {

    private static final String RPC_QUEUE_NAME = "rpcQueue";

    /**
     * 调用接口
     *
     * @param message 消息内容 不能为空
     */
    public void client(String message) throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        String replyQueueName = channel.getChannel().queueDeclare().getQueue();
        String corrId = UUID.randomUUID().toString();

        AMQP.BasicProperties props = new AMQP.BasicProperties
                .Builder()
                .correlationId(corrId)
                .replyTo(replyQueueName)
                .build();
        System.out.println("rpc客户端发送消息:" + message);
        channel.getChannel().basicPublish("", RPC_QUEUE_NAME, props, message.getBytes("UTF-8"));
        channel.getChannel().basicConsume(replyQueueName, true, new DefaultConsumer(channel.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                if (properties.getCorrelationId().equals(corrId)) {
                    System.out.println("rpc客户端收到结果:" + new String(body, "UTF-8") + "\n");
                }
            }
        });
    }

    /**
     * 服务端开启服务
     */
    public void service() throws IOException, TimeoutException {
        RabbitMQChannel channel = new RabbitMQChannel().channel();
        channel.getChannel().queueDeclare(RPC_QUEUE_NAME, false, false, false, null);
        channel.getChannel().basicQos(1);
        System.out.println("等待rpc客户端连接...");

        Consumer consumer = new DefaultConsumer(channel.getChannel()) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                AMQP.BasicProperties replyProps = new AMQP.BasicProperties
                        .Builder()
                        .correlationId(properties.getCorrelationId())
                        .build();
                String response = "";
                try {
                    String message = new String(body, "UTF-8");
                    System.out.println("服务端接受到消息:" + message);
                    response = message + UUID.randomUUID().toString();
                } catch (RuntimeException e) {
                    e.printStackTrace();
                } finally {
                    channel.getChannel().basicPublish("", properties.getReplyTo(), replyProps, response.getBytes("UTF-8"));
                    channel.getChannel().basicAck(envelope.getDeliveryTag(), false);
                    System.out.println("服务端将处理结果:" + response + ",返回客户单\n");
                }
            }
        };
        channel.getChannel().basicConsume(RPC_QUEUE_NAME, false, consumer);
    }
}
