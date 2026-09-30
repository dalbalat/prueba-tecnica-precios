package com.dario.pricingservice.application.service;

import com.dario.pricingservice.application.port.out.PriceQueryPort;
import com.dario.pricingservice.domain.model.Price;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FindApplicablePriceServiceTest {

    private static final LocalDateTime APPLICATION_DATE =
            LocalDateTime.of(2020, 6, 14, 16, 0);

    private static final Long PRODUCT_ID = 35455L;
    private static final Long BRAND_ID = 1L;

    private static final Price EXPECTED_PRICE = new Price(
            BRAND_ID,
            LocalDateTime.of(2020, 6, 14, 15, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30),
            2L,
            PRODUCT_ID,
            1,
            new BigDecimal("25.45"),
            "EUR"
    );

    @Mock
    private PriceQueryPort priceQueryPort;

    private FindApplicablePriceService service;

    @BeforeEach
    void setUp() {
        service = new FindApplicablePriceService(priceQueryPort);
    }

    @Test
    void shouldReturnApplicablePriceUsingExactQueryParameters() {
        when(priceQueryPort.findApplicablePrice(
                APPLICATION_DATE,
                PRODUCT_ID,
                BRAND_ID
        )).thenReturn(Optional.of(EXPECTED_PRICE));

        Optional<Price> result = service.findApplicablePrice(
                APPLICATION_DATE,
                PRODUCT_ID,
                BRAND_ID
        );

        assertThat(result).contains(EXPECTED_PRICE);

        verify(priceQueryPort).findApplicablePrice(
                APPLICATION_DATE,
                PRODUCT_ID,
                BRAND_ID
        );
        verifyNoMoreInteractions(priceQueryPort);
    }

    @Test
    void shouldReturnEmptyWhenPortFindsNoApplicablePrice() {
        when(priceQueryPort.findApplicablePrice(
                APPLICATION_DATE,
                PRODUCT_ID,
                BRAND_ID
        )).thenReturn(Optional.empty());

        Optional<Price> result = service.findApplicablePrice(
                APPLICATION_DATE,
                PRODUCT_ID,
                BRAND_ID
        );

        assertThat(result).isEmpty();

        verify(priceQueryPort).findApplicablePrice(
                APPLICATION_DATE,
                PRODUCT_ID,
                BRAND_ID
        );
        verifyNoMoreInteractions(priceQueryPort);
    }

    @Test
    void shouldRejectNullApplicationDateWithoutQueryingPort() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findApplicablePrice(
                        null,
                        PRODUCT_ID,
                        BRAND_ID
                ))
                .withMessage("applicationDate no puede ser null");

        verifyNoInteractions(priceQueryPort);
    }

    @ParameterizedTest(name = "productId={0} debe rechazarse sin consultar el puerto")
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void shouldRejectInvalidProductIdWithoutQueryingPort(Long productId) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findApplicablePrice(
                        APPLICATION_DATE,
                        productId,
                        BRAND_ID
                ))
                .withMessage("productId debe ser positivo");

        verifyNoInteractions(priceQueryPort);
    }

    @ParameterizedTest(name = "brandId={0} debe rechazarse sin consultar el puerto")
    @NullSource
    @ValueSource(longs = {0L, -1L})
    void shouldRejectInvalidBrandIdWithoutQueryingPort(Long brandId) {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findApplicablePrice(
                        APPLICATION_DATE,
                        PRODUCT_ID,
                        brandId
                ))
                .withMessage("brandId debe ser positivo");

        verifyNoInteractions(priceQueryPort);
    }
}