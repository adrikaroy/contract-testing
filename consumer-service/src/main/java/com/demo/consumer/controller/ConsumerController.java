package com.demo.consumer.controller;

import com.demo.consumer.request.CalculatorRequest;
import com.demo.consumer.response.ConsumerResponse;
import com.demo.consumer.response.SuccessResponse;
import com.demo.consumer.service.ConsumerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/consumer")
@Validated
public class ConsumerController {

    private final ConsumerService consumerService;

    public ConsumerController(ConsumerService consumerService) {
        this.consumerService = consumerService;
    }

    @GetMapping("/add")
    public SuccessResponse<ConsumerResponse> add(@RequestParam Double number1, Double number2) {
        CalculatorRequest calculatorRequest =
                CalculatorRequest.builder().number1(number1).number2(number2).build();
        return SuccessResponse.ok(consumerService.add(calculatorRequest));
    }
}
