package com.demo.consumer.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class ConsumerResponse {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double addition;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double subtraction;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double multiplication;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Double division;
}
