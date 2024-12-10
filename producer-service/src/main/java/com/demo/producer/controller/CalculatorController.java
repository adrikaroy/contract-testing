package com.demo.producer.controller;

import com.demo.producer.request.CalculatorRequest;
import com.demo.producer.response.CalculatorResponse;
import com.demo.producer.response.SuccessResponse;
import com.demo.producer.service.CalculatorService;
import com.demo.producer.util.OperationType;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/producer/calculate")
@Validated
public class CalculatorController {
    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService) {
        this.calculatorService = calculatorService;
    }

    @PostMapping
    public SuccessResponse<CalculatorResponse> calculate(
            @RequestParam OperationType operation, @RequestBody @Valid CalculatorRequest calculatorRequest) {
        return SuccessResponse.ok(calculatorService.calculate(operation, calculatorRequest));
    }
}
