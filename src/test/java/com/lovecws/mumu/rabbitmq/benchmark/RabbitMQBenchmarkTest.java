package com.lovecws.mumu.rabbitmq.benchmark;

import com.lovecws.mumu.rabbitmq.RabbitMQChannel;
import org.junit.Test;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;

/**
 * 使用jmh测试 rabbitmq并发
 */
public class RabbitMQBenchmarkTest {

    private static final RabbitMQChannel channel = new RabbitMQChannel().channel();
    private static final String BENCHMARK_QUEUE = "benchmarkqueue";
    private static final byte[] MESSAGE = new byte[10];

    static {
        try {
            //TODO 如果queue不存在，当然Consumer不会得到任何的Message。但是如果queue不存在，那么Producer Publish的Message会被丢弃。所以，还是为了数据不丢失，Consumer和Producer都try to create the queue！反正不管怎么样，这个接口都不会出问题。
            channel.getChannel().queueDeclare(BENCHMARK_QUEUE, false, false, false, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    public void sendMessage() throws IOException {
        channel.getChannel().basicPublish("", BENCHMARK_QUEUE, null, MESSAGE);
    }

    /**
     * jmh测试结果展示
     * 线程数量  预热时间 测量时间  消息大小  并发量tps/s
     * 1         10       10        10b       23140
     * 10        10       10        10        31255
     * 20        10       10        10        33032
     * TODO 怎么感觉增加线程的时候 并发量不是提升的特别多啊，是不是建立链接的时候，rabbitmq自动创建了连接池。而且当发送消息的时候，服务cpu占用特别高，都超过100%了。
     * @throws RunnerException
     */
    @Test
    public void testSendMessage() throws RunnerException {
        Options options = new OptionsBuilder()
                .include(RabbitMQBenchmarkTest.class.getSimpleName() + ".sendMessage$")
                .warmupIterations(10)
                .measurementIterations(10)
                .threads(20)
                .forks(1)
                .shouldDoGC(false)
                .shouldFailOnError(true)
                .build();
        new Runner(options).run();
    }

}
