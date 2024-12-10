package com.demo.consumer.contract;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

import au.com.dius.pact.consumer.MockServer;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.demo.consumer.request.CalculatorRequest;
import com.demo.consumer.response.CalculatorResponse;
import com.demo.consumer.response.SuccessResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Calculator service", pactVersion = PactSpecVersion.V3)
class CalculatorConsumerTest {

    String request = """
        {
          "number1": 10.1,
          "number2": 12.1
        }""";

    String response =
            """
        {
            "status": "SUCCESS",
            "data": {
                "output": 22.2
            }
        }""";

    @Pact(consumer = "Addition service", provider = "Calculator service")
    public RequestResponsePact addNumbersPact(PactDslWithProvider builder) {

        return builder.given("Addition")
                .uponReceiving("Add two numbers")
                .path("/producer/calculate")
                .query("operation=ADD")
                .method("POST")
                .headers(Map.of("Content-Type", MediaType.APPLICATION_JSON_VALUE))
                .body(request)
                .willRespondWith()
                .status(200)
                .body(response)
                .toPact();
    }

    @Test
    @PactTestFor(providerName = "Calculator service", pactMethod = "addNumbersPact")
    void shouldAddNumbers(MockServer mockServer) {
        RestTemplate restTemplate = new RestTemplateBuilder().build();
        HttpHeaders headers = new HttpHeaders();
        List<HttpMessageConverter<?>> messageConverters = new ArrayList<>();
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(APPLICATION_JSON, APPLICATION_OCTET_STREAM));
        messageConverters.add(converter);
        restTemplate.setMessageConverters(messageConverters);
        CalculatorRequest calculatorRequest =
                CalculatorRequest.builder().number1(10.1).number2(12.1).build();
        HttpEntity<CalculatorRequest> httpRequest = new HttpEntity<>(calculatorRequest, headers);

        ResponseEntity<SuccessResponse<CalculatorResponse>> responseEntity = restTemplate.exchange(
                mockServer.getUrl() + "/producer/calculate?operation={operation}",
                POST,
                httpRequest,
                new ParameterizedTypeReference<>() {},
                "ADD");

        assertThat(responseEntity.getStatusCode()).isEqualTo(OK);
        assertNotNull(responseEntity.getBody());
        assertThat(responseEntity.getBody().getData().getOutput()).isEqualTo(22.2);
    }
}
