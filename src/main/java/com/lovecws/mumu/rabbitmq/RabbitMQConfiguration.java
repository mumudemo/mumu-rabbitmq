package com.lovecws.mumu.rabbitmq;

public class RabbitMQConfiguration {

    public static String RABBITMQ_HOST = null;

    static {
        //从环境变量中 获取
        String RABBITMQHOST = System.getenv("RABBITMQ_HOST");
        if (RABBITMQHOST != null) {
            RABBITMQ_HOST = RABBITMQHOST;
        } else {
            RABBITMQ_HOST = "192.168.11.25";
        }
    }
}
