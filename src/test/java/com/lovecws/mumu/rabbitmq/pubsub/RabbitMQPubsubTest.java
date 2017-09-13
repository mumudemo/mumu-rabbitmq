package com.lovecws.mumu.rabbitmq.pubsub;

import com.lovecws.mumu.rabbitmq.workqueue.RabbitMQWorkQueue;
import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMQPubsubTest {

    private RabbitMQPubsub rabbitMQPubsub = new RabbitMQPubsub();

    @Test
    public void sendPubsubMessage() throws IOException, TimeoutException {
        for (int i = 0; i < 10; i++) {
            rabbitMQPubsub.sendPubsubMessage("lovecws"+ new Random().nextInt(1000));
        }
    }

    @Test
    public void receivePubsubMessage() throws IOException, TimeoutException, InterruptedException {
        rabbitMQPubsub.receivePubsubMessage();
        TimeUnit.SECONDS.sleep(100);
    }
}
