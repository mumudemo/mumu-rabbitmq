package com.lovecws.mumu.rabbitmq.quickstart;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMQQuickStartTest {

    private RabbitMQQuickStart rabbitMQQuickStart=new RabbitMQQuickStart();

    @Test
    public void sendQuickstartMessage() throws IOException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            rabbitMQQuickStart.sendQuickstartMessage("lovecws"+new Random().nextInt(1000));
        }
    }

    @Test
    public void receiveQuickstartMessage() throws IOException, TimeoutException, InterruptedException {
        rabbitMQQuickStart.receiveQuickstartMessage();
        TimeUnit.SECONDS.sleep(10);
    }
}
