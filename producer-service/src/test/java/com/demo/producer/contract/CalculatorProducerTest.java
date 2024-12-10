package com.demo.producer.contract;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junitsupport.Provider;
import au.com.dius.pact.provider.junitsupport.State;
import au.com.dius.pact.provider.junitsupport.loader.PactBroker;
import au.com.dius.pact.provider.spring.junit5.MockMvcTestTarget;
import au.com.dius.pact.provider.spring.junit5.PactVerificationSpringProvider;
import com.demo.producer.controller.CalculatorController;
import com.demo.producer.request.CalculatorRequest;
import com.demo.producer.response.CalculatorResponse;
import com.demo.producer.service.CalculatorService;
import com.demo.producer.util.OperationType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

@Provider("Calculator service")
@PactBroker(url = "http://localhost:9292")
class CalculatorProducerTest {

    private CalculatorService calculatorService;

    @TestTemplate
    @ExtendWith(PactVerificationSpringProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        context.verifyInteraction();
    }

    @BeforeEach
    void before(PactVerificationContext context) {
        System.setProperty("pact.verifier.publishResults", "true");
        MockMvcTestTarget testTarget = new MockMvcTestTarget();
        calculatorService = mock(CalculatorService.class);
        testTarget.setControllers(new CalculatorController(calculatorService));
        context.setTarget(testTarget);
    }

    @State("Addition")
    void shouldAddNumbers() {
        CalculatorResponse calculatorResponse =
                CalculatorResponse.builder().output(22.2).build();
        when(calculatorService.calculate(eq(OperationType.ADD), any(CalculatorRequest.class)))
                .thenReturn(calculatorResponse);
    }
}
