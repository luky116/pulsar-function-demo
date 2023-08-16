package com.sanyue.pulsar.function.pulsarfunctiondemo.demo1;

import org.apache.pulsar.client.api.*;

public class ProducerApp {

    public static void main(String[] args) throws PulsarClientException {
        PulsarClient client = PulsarClient.builder()
                .serviceUrl(Consts.SERVER_URL)
                .build();

        Producer<String> producer = client.newProducer(Schema.STRING)
                .topic(Consts.INPUT_TOPIC)
                .producerName("func-pro1")
                .create();

        MessageId messageId = producer.send("Hello,lzhpo,lewis");
        System.out.println("Send ok, messageId:" + messageId);

        client.close();
    }
}
