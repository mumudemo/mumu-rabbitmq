package com.lovecws.mumu.rabbitmq.rpc;

import org.junit.Test;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class RabbitMQRPCTest {
    private RabbitMQRPC rabbitMQRPC = new RabbitMQRPC();

    @Test
    public void client() throws IOException, TimeoutException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            rabbitMQRPC.client("lovecws" + new Random().nextInt(1000));
        }
        TimeUnit.SECONDS.sleep(10);
    }

    @Test
    public void service() throws IOException, TimeoutException, InterruptedException {
        rabbitMQRPC.service();
        TimeUnit.SECONDS.sleep(100);
    }
}
