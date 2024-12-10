package com.demo.consumer.service;

import com.demo.consumer.client.ProducerClient;
import com.demo.consumer.request.CalculatorRequest;
import com.demo.consumer.response.ConsumerResponse;
import org.springframework.stereotype.Service;

@Service
public class ConsumerService {
    private final ProducerClient producerClient;

    public ConsumerService(ProducerClient producerClient) {
        this.producerClient = producerClient;
    }

    public ConsumerResponse add(CalculatorRequest calculatorRequest) {
        Double producerResponse = producerClient.getProducerResponse(calculatorRequest);
        return ConsumerResponse.builder().addition(producerResponse).build();
    }
}
