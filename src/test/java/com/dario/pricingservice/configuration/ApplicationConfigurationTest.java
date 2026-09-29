package com.dario.pricingservice.configuration;

import com.dario.pricingservice.application.service.FindApplicablePriceService;
import com.dario.pricingservice.application.port.in.FindApplicablePriceUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class ApplicationConfigurationTest {

    @Autowired
    private FindApplicablePriceUseCase findApplicablePriceUseCase;

    @Test
    void shouldRegisterFindApplicablePriceUseCaseBean() {
        assertThat(findApplicablePriceUseCase)
                .isNotNull()
                .isInstanceOf(FindApplicablePriceService.class);
    }
}
