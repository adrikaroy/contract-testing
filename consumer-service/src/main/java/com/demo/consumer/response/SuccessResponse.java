package com.demo.consumer.response;

import com.demo.consumer.util.ResponseStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.io.Serializable;
import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessResponse<T> implements Serializable {
    private ResponseStatus status;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private transient T data;

    public static <T> SuccessResponse<T> ok(T body) {
        SuccessResponse<T> response = new SuccessResponse<>();
        response.data = body;
        response.status = ResponseStatus.SUCCESS;
        return response;
    }
}
