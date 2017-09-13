package com.lovecws.mumu.rabbitmq;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 获取到channel
 */
public class RabbitMQChannel {

    private volatile boolean init = false;
    private Connection connection;
    private Channel channel;

    /**
     * 获取到rabbitmq channel
     *
     * @return
     * @throws IOException
     * @throws TimeoutException
     */
    public RabbitMQChannel channel(){
        if (!init) {
            synchronized (this) {
                if (!init) {
                    ConnectionFactory factory = new ConnectionFactory();
                    factory.setHost(RabbitMQConfiguration.RABBITMQ_HOST);
                    if(!RabbitMQConfiguration.RABBITMQ_HOST.equalsIgnoreCase("localhost")&&!RabbitMQConfiguration.RABBITMQ_HOST.equalsIgnoreCase("127.0.0.1")){
                        factory.setUsername("babymm");
                        factory.setPassword("root");
                    }
                    try {
                        connection = factory.newConnection();
                        channel = connection.createChannel();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (TimeoutException e) {
                        e.printStackTrace();
                    }

                }
                init = true;
            }
        }
        return this;
    }

    /**
     * 关闭资料
     */
    public void close() {
        try {
            if (channel != null) {
                channel.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (IOException | TimeoutException e) {
            e.printStackTrace();
        }
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(final Connection connection) {
        this.connection = connection;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(final Channel channel) {
        this.channel = channel;
    }

    @Override
    public String toString() {
        return "{connection:"+connection+",channel:"+channel+"}";
    }
}
