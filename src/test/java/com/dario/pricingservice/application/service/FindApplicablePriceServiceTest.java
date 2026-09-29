package com.dario.pricingservice.application.service;

import com.dario.pricingservice.domain.model.Price;
import com.dario.pricingservice.application.port.out.PriceQueryPort;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class FindApplicablePriceServiceTest {

    private static final LocalDateTime APPLICATION_DATE =
            LocalDateTime.of(2020, 6, 14, 16, 0);

    private static final Price EXPECTED_PRICE = new Price(
            1L,
            LocalDateTime.of(2020, 6, 14, 15, 0),
            LocalDateTime.of(2020, 6, 14, 18, 30),
            2L,
            35455L,
            1,
            new BigDecimal("25.45"),
            "EUR"
    );

    @Test
    void shouldReturnApplicablePrice() {
        PriceQueryPort priceQueryPort =
                (applicationDate, productId, brandId) ->
                        Optional.of(EXPECTED_PRICE);

        FindApplicablePriceService service =
                new FindApplicablePriceService(priceQueryPort);

        Optional<Price> result = service.findApplicablePrice(
                APPLICATION_DATE,
                35455L,
                1L
        );

        assertThat(result)
                .isPresent()
                .contains(EXPECTED_PRICE);
    }

    @Test
    void shouldReturnEmptyWhenThereIsNoApplicablePrice() {
        PriceQueryPort priceQueryPort =
                (applicationDate, productId, brandId) ->
                        Optional.empty();

        FindApplicablePriceService service =
                new FindApplicablePriceService(priceQueryPort);

        Optional<Price> result = service.findApplicablePrice(
                APPLICATION_DATE,
                35455L,
                1L
        );

        assertThat(result).isEmpty();
    }

    @Test
    void shouldRejectNullApplicationDate() {
        PriceQueryPort priceQueryPort =
                (applicationDate, productId, brandId) ->
                        Optional.of(EXPECTED_PRICE);

        FindApplicablePriceService service =
                new FindApplicablePriceService(priceQueryPort);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findApplicablePrice(
                        null,
                        35455L,
                        1L
                ))
                .withMessage("applicationDate no puede ser null");
    }

    @Test
    void shouldRejectInvalidProductId() {
        PriceQueryPort priceQueryPort =
                (applicationDate, productId, brandId) ->
                        Optional.of(EXPECTED_PRICE);

        FindApplicablePriceService service =
                new FindApplicablePriceService(priceQueryPort);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findApplicablePrice(
                        APPLICATION_DATE,
                        0L,
                        1L
                ))
                .withMessage("productId debe ser positivo");
    }

    @Test
    void shouldRejectInvalidBrandId() {
        PriceQueryPort priceQueryPort =
                (applicationDate, productId, brandId) ->
                        Optional.of(EXPECTED_PRICE);

        FindApplicablePriceService service =
                new FindApplicablePriceService(priceQueryPort);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> service.findApplicablePrice(
                        APPLICATION_DATE,
                        35455L,
                        0L
                ))
                .withMessage("brandId debe ser positivo");
    }
}