package com.dario.pricingservice.configuration;

import com.dario.pricingservice.application.service.FindApplicablePriceService;
import com.dario.pricingservice.domain.port.input.FindApplicablePriceUseCase;
import com.dario.pricingservice.domain.port.output.PriceQueryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ApplicationConfiguration {

    @Bean
    public FindApplicablePriceUseCase findApplicablePriceUseCase(
            PriceQueryPort priceQueryPort
    ) {
        return new FindApplicablePriceService(priceQueryPort);
    }
}
