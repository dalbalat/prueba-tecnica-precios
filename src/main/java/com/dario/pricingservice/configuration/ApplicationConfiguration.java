package com.dario.pricingservice.configuration;

import com.dario.pricingservice.adapter.input.rest.PriceResponseMapper;
import com.dario.pricingservice.adapter.output.persistence.adapter.PricePersistenceAdapter;
import com.dario.pricingservice.adapter.output.persistence.mapper.PricePersistenceMapper;
import com.dario.pricingservice.adapter.output.persistence.repository.PriceJpaRepository;
import com.dario.pricingservice.application.port.in.FindApplicablePriceUseCase;
import com.dario.pricingservice.application.port.out.PriceQueryPort;
import com.dario.pricingservice.application.service.FindApplicablePriceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfiguration {

    @Bean
    public PricePersistenceMapper pricePersistenceMapper() {
        return new PricePersistenceMapper();
    }

    @Bean
    public PriceQueryPort priceQueryPort(
            PriceJpaRepository priceJpaRepository,
            PricePersistenceMapper pricePersistenceMapper
    ) {
        return new PricePersistenceAdapter(
                priceJpaRepository,
                pricePersistenceMapper
        );
    }

    @Bean
    public FindApplicablePriceUseCase findApplicablePriceUseCase(
            PriceQueryPort priceQueryPort
    ) {
        return new FindApplicablePriceService(priceQueryPort);
    }

    @Bean
    public PriceResponseMapper priceResponseMapper() {
        return new PriceResponseMapper();
    }
}
