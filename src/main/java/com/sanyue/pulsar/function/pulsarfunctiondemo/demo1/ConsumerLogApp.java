package com.sanyue.pulsar.function.pulsarfunctiondemo.demo1;

import lombok.extern.slf4j.Slf4j;
import org.apache.pulsar.client.api.*;

import java.util.concurrent.TimeUnit;

@Slf4j
public class ConsumerLogApp {


    public static void main(String[] args) throws Exception {
        // 构造Pulsar Client
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(Consts.SERVER_URL)
                .enableTcpNoDelay(true)
                .build();
        Consumer consumer = client.newConsumer()
                .consumerName("my-consumer")
                .topic(Consts.LOG_TOPIC)
                .subscriptionName("my-subscription")
                .ackTimeout(10, TimeUnit.SECONDS)
                .maxTotalReceiverQueueSizeAcrossPartitions(10)
                .subscriptionType(SubscriptionType.Exclusive)
                .batchReceivePolicy(BatchReceivePolicy.builder()
                        .maxNumMessages(100)
                        .maxNumBytes(1024 * 1024)
                        .timeout(100, TimeUnit.MILLISECONDS)
                        .build())
                .subscribe();
        do {
            // 接收消息有两种方式：异步和同步
            // CompletableFuture<Message<String>> message = consumer.receiveAsync();
            Message message = consumer.receive();
            System.out.println("【LOG TOPIC】消息 {} " + new String(message.getData(), "utf-8"));
            consumer.acknowledge(message);
        } while (true);
    }
}