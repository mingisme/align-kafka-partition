package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProducerController {

    @Autowired
    KafkaTemplate<String, String> template;

    @GetMapping("send")
    public String send(@RequestParam String topic, @RequestParam String key) {
        try {

            template.send(topic, key, key);

            return "success";
        } catch (Throwable t) {
            t.printStackTrace();
            return t.getMessage();
        }
    }
}
