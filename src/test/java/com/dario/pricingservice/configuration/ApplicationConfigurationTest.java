package com.dario.pricingservice.configuration;

import com.dario.pricingservice.adapter.input.rest.PriceResponseMapper;
import com.dario.pricingservice.adapter.output.persistence.adapter.PricePersistenceAdapter;
import com.dario.pricingservice.adapter.output.persistence.mapper.PricePersistenceMapper;
import com.dario.pricingservice.adapter.output.persistence.repository.PriceJpaRepository;
import com.dario.pricingservice.application.port.in.FindApplicablePriceUseCase;
import com.dario.pricingservice.application.port.out.PriceQueryPort;
import com.dario.pricingservice.application.service.FindApplicablePriceService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.runner.ApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class ApplicationConfigurationTest {

    private final ApplicationContextRunner contextRunner =
            new ApplicationContextRunner()
                    .withBean(
                            PriceJpaRepository.class,
                            () -> mock(PriceJpaRepository.class)
                    )
                    .withUserConfiguration(ApplicationConfiguration.class);

    @Test
    void shouldWireApplicationUseCaseThroughPortsAndMappers() {
        contextRunner.run(context -> {
            assertThat(context.getBean(FindApplicablePriceUseCase.class))
                    .isInstanceOf(FindApplicablePriceService.class);

            assertThat(context.getBean(PriceQueryPort.class))
                    .isInstanceOf(PricePersistenceAdapter.class);

            assertThat(context.getBean(PricePersistenceMapper.class))
                    .isNotNull();

            assertThat(context.getBean(PriceResponseMapper.class))
                    .isNotNull();
        });
    }
}
