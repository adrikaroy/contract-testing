package com.demo.consumer.request;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class CalculatorRequest {
    private Double number1;
    private Double number2;
}
