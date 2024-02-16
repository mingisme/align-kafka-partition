package com.example;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.*;
import org.springframework.kafka.listener.ConsumerAwareRebalanceListener;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@SpringBootApplication
public class App {
    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Bean
    public ApplicationRunner runner() {
        return args -> {

        };
    }

    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:58896");
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.GROUP_ID_CONFIG, "align_group");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "latest");
        return props;
    }

    @Bean
    public ConsumerFactory<String, String> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
//        factory.setConcurrency(5);
        factory.getContainerProperties().setConsumerRebalanceListener(new CustomRebalanceListener());
        return factory;
    }

    @KafkaListener(id="t1Listener", topics = "t1")
    public void listenT1(String message) {
        System.out.println("Received message from t1: " + message);
    }

    @KafkaListener(id="t2Listener", topics = "t2")
    public void listenT2(String message) {
        System.out.println("Received message from t2: " + message);
    }

    private class CustomRebalanceListener implements ConsumerAwareRebalanceListener {
        @Override
        public void onPartitionsAssigned(Consumer<?, ?> consumer, Collection<TopicPartition> partitions) {
            Map<Integer, Integer> partitionMapping = mapPartitions(partitions.size());
//            partitions.forEach(partition -> {
//                int correspondingPartition = partitionMapping.get(partition.partition());
//                consumer.assign(Collections.singletonList(new TopicPartition("t2", correspondingPartition)));
//            });
        }

        private Map<Integer, Integer> mapPartitions(int numPartitions) {
            Map<Integer, Integer> mapping = new HashMap<>();
            for (int i = 0; i < numPartitions; i++) {
                mapping.put(i, i);
            }
            return mapping;
        }
    }

}