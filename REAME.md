# mumu-rabbitmq 消息中间件
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/mumudemo/mumu-rabbitmq/blob/master/LICENSE) 
[![Maven Central](https://img.shields.io/maven-central/v/com.weibo/motan.svg?label=Maven%20Central)](https://github.com/mumudemo/mumu-rabbitmq) 
[![Build Status](https://travis-ci.org/mumudemo/mumu-rabbitmq.svg?branch=master)](https://travis-ci.org/mumudemo/mumu-rabbitmq)
[![codecov](https://codecov.io/gh/mumudemo/mumu-rabbitmq/branch/master/graph/badge.svg)](https://codecov.io/gh/mumudemo/mumu-rabbitmq)
[![OpenTracing-1.0 Badge](https://img.shields.io/badge/OpenTracing--1.0-enabled-blue.svg)](http://opentracing.io)

***RabbitMQ is the most widely deployed open source message broker.***

## rabbitmq简介
   RabbitMQ是一个由erlang开发的AMQP（Advanced Message Queue ）的开源实现。AMQP 的出现其实也是应了广大人民群众的需求，虽然在同步消息通讯的世界里有很多公开标准（如 COBAR的 IIOP ，或者是 SOAP 等），但是在异步消息处理中却不是这样，只有大企业有一些商业实现（如微软的 MSMQ ，IBM 的 Websphere MQ 等），因此，在 2006 年的 6 月，Cisco 、Redhat、iMatix 等联合制定了 AMQP 的公开标准。
## rabbitmq 原理  
![rabbitmq原理图](http://img.blog.csdn.net/20140220173559828)

**Exchanges**  
 -  Direct exchange: 如果 routing key 匹配, 那么Message就会被传递到相应的queue中。其实在queue创建时，它会自动的以queue的名字作为routing key来绑定那个exchange。  

-  Fanout exchange: 会向响应的queue广播。  

-  Topic exchange: 对key进行模式匹配，比如ab*可以传递到所有ab*的queue。

**queue**   
- 消费者从绑定的队列中获取到消息。  

**Bindings**  
-  绑定Exchanges和queue之间的关系，在订阅的时候，消费者可以选择一个队列绑定到exchange上，然后可以获取发送到exchange上的消息。
 
**connection、channel**  
> connection:就是一个TCP的连接。Producer和Consumer都是通过TCP连接到RabbitMQ Server的。以后我们可以看到，程序的起始处就是建立这个TCP连接。  
>
> Channels:虚拟连接。它建立在上述的TCP连接中。数据流动都是在Channel中进行的。也就是说，一般情况是程序起始建立TCP连接，第二步就是建立这个Channel。  
>
>那么，为什么使用Channel，而不是直接使用TCP连接,对于OS来说，建立和关闭TCP连接是有代价的，频繁的建立关闭TCP连接对于系统的性能有很大的影响，而且TCP的连接数也有限制，这也限制了系统处理高并发的能力。但是，在TCP连接中建立Channel是没有上述代价的

## rabbitmq 代码展示
**消息发送**
```
public void sendMessage(String message) throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfiguration.RABBITMQ_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUICKSTART_QUEUE_NAME, false, false, false, null);
        channel.basicPublish("", QUICKSTART_QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送成功:" + message);
        channel.close();
        connection.close();
    }
```
**消息接受**
```
public void receiveMessage() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(RabbitMQConfiguration.RABBITMQ_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUICKSTART_QUEUE_NAME, false, false, false, null);
        //建立一个消费者 监听消息的接受
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("接受消息:" + message);
            }
        };
        channel.basicConsume(QUICKSTART_QUEUE_NAME, true, consumer);
    }
```
## rabbitmq测试
```
     * 线程数量  预热时间 测量时间  消息大小  并发量tps/s
     * 1         10       10        10b       23140
     * 10        10       10        10b       31255
     * 20        10       10        10b       33032
```
不知道是不是我的测试方法错了，随着线程数量的增多，而tps却没有增大。这块有待进一步确认。

## 相关阅读  
[rabbitmq官网文档](https://www.rabbitmq.com/)   
[RabbitMQ从入门到精通](http://blog.csdn.net/column/details/rabbitmq.html)

## 联系方式
**以上观点纯属个人看法，如有不同，欢迎指正。  
email:<babymm@aliyun.com>  
github:[https://github.com/babymm](https://github.com/babymm)**