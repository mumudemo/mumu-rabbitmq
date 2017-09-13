package com.lovecws.mumu.rabbitmq.workqueue;

import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMQWorkQueueTest {

    private RabbitMQWorkQueue rabbitMQWorkQueue = new RabbitMQWorkQueue();

    @Test
    public void sendWorkQueueMessage() throws IOException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            rabbitMQWorkQueue.sendWorkQueueMessage("lovecws");
        }
    }

    @Test
    public void receiveWorkQueueMessage() throws IOException, TimeoutException, InterruptedException {
        rabbitMQWorkQueue.receiveWorkQueueMessage();
        TimeUnit.SECONDS.sleep(10);
    }
}
