package com.demo.producer.response;

import com.demo.producer.util.ResponseStatus;
import java.util.List;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private ResponseStatus status;
    private List<String> message;
}
