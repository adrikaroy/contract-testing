package com.demo.producer.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class CalculatorRequest {
    @NotNull(message = "number1 must not be null")
    private Double number1;

    @NotNull(message = "number2 must not be null")
    private Double number2;
}
