package com.demo.producer.service;

import com.demo.producer.exception.CustomException;
import com.demo.producer.request.CalculatorRequest;
import com.demo.producer.response.CalculatorResponse;
import com.demo.producer.util.OperationType;
import org.springframework.stereotype.Service;

@Service
public class CalculatorService {
    public CalculatorResponse calculate(OperationType operationType, CalculatorRequest calculatorRequest) {
        return CalculatorResponse.builder()
                .output(getCalculatorResponse(operationType, calculatorRequest))
                .build();
    }

    private Double getCalculatorResponse(OperationType operationType, CalculatorRequest calculatorRequest) {
        return switch (operationType) {
            case ADD -> calculatorRequest.getNumber1() + calculatorRequest.getNumber2();
            case SUBTRACT -> calculatorRequest.getNumber1() - calculatorRequest.getNumber2();
            case MULTIPLY -> calculatorRequest.getNumber1() * calculatorRequest.getNumber2();
            case DIVIDE -> {
                if (calculatorRequest.getNumber2() == 0) {
                    throw new CustomException("Cannot divide by 0");
                }
                yield calculatorRequest.getNumber1() / calculatorRequest.getNumber2();
            }
        };
    }
}
