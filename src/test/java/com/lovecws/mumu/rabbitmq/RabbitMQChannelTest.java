package com.lovecws.mumu.rabbitmq;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitMQChannelTest {

    @Test
    public void channel() throws IOException, TimeoutException {
        RabbitMQChannel rabbitMQChannel = new RabbitMQChannel().channel();
        System.out.println(rabbitMQChannel.getChannel());
    }

    @Test
    public void close() throws IOException, TimeoutException {
        RabbitMQChannel rabbitMQChannel = new RabbitMQChannel().channel();
        rabbitMQChannel.getChannel().close();
    }
}
