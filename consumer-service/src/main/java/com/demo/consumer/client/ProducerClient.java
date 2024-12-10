package com.demo.consumer.client;

import com.demo.consumer.exception.CustomException;
import com.demo.consumer.request.CalculatorRequest;
import com.demo.consumer.response.CalculatorResponse;
import com.demo.consumer.response.SuccessResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ProducerClient {

    @Value("${producer-service.url}")
    String producerUrl;

    private final RestTemplate restTemplate;

    public ProducerClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public Double getProducerResponse(CalculatorRequest calculatorRequest) {
        String url = producerUrl + "/producer/calculate?operation=ADD";
        HttpEntity<CalculatorRequest> httpRequest = buildHttpRequest(calculatorRequest);

        ResponseEntity<SuccessResponse<CalculatorResponse>> response;
        try {
            response = restTemplate.exchange(url, HttpMethod.POST, httpRequest, new ParameterizedTypeReference<>() {});
        } catch (RestClientException restClientException) {
            throw new CustomException("Error occurred while calling producer service");
        }

        SuccessResponse<CalculatorResponse> successResponse = response.getBody();
        throwExceptionIfResponseIsNull(successResponse);

        return successResponse.getData().getOutput();
    }

    private static void throwExceptionIfResponseIsNull(SuccessResponse<CalculatorResponse> successResponse) {
        if (successResponse == null || successResponse.getData() == null) {
            throw new CustomException("Invalid response from producer service");
        }
    }

    private HttpEntity<CalculatorRequest> buildHttpRequest(CalculatorRequest calculatorRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(calculatorRequest, headers);
    }
}
