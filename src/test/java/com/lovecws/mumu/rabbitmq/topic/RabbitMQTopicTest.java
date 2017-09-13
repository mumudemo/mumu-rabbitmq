package com.lovecws.mumu.rabbitmq.topic;

import com.lovecws.mumu.rabbitmq.quickstart.RabbitMQQuickStart;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMQTopicTest {

    private RabbitMQTopic rabbitMQTopic=new RabbitMQTopic();

    @Test
    public void sendQuickstartMessage() throws IOException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            rabbitMQTopic.sendTopicMessage("lovecws"+new Random().nextInt(1000));
        }
    }

    @Test
    public void receiveQuickstartMessage() throws IOException, TimeoutException, InterruptedException {
        rabbitMQTopic.receiveTopicMessage();
        TimeUnit.SECONDS.sleep(10);
    }
}
