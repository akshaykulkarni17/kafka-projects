package com.example.kafka.tutorial1;

import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ProducerWithCallback {

    public static void main(String[] args) {

        final Logger logger = LoggerFactory.getLogger(ProducerWithCallback.class);

        String bootstrapServer = "localhost:9092";

        Properties properties = new Properties();
        properties.setProperty(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,bootstrapServer);
        properties.setProperty(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,StringSerializer.class.getName());

        KafkaProducer<String,String> producer = new KafkaProducer<>(properties);

        for (int i = 1; i < 10; i++) {
            ProducerRecord<String,String> record = new ProducerRecord<>("first-topic","world " + i);

            producer.send(record,(recordMetadata, e) -> {
                if (e==null){
                    logger.info("Topic: "+recordMetadata.topic()+"\nPartitions: "+recordMetadata.partition()+
                            "\nTime: "+recordMetadata.timestamp());
                }
                else {
                    logger.error("Error occurred: "+e);
                }
            });
        }

        producer.flush();
        producer.close();

    }
}
