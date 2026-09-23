package com.dario.pricingservice.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class PriceTest {

    private static final LocalDateTime START_DATE =
            LocalDateTime.of(2020, 6, 14, 0, 0);

    private static final LocalDateTime END_DATE =
            LocalDateTime.of(2020, 12, 31, 23, 59, 59);

    @Test
    void shouldCreateValidPrice() {
        Price price = new Price(
                1L,
                START_DATE,
                END_DATE,
                1L,
                35455L,
                0,
                new BigDecimal("35.50"),
                "EUR"
        );

        assertThat(price.brandId()).isEqualTo(1L);
        assertThat(price.priceList()).isEqualTo(1L);
        assertThat(price.productId()).isEqualTo(35455L);
        assertThat(price.price()).isEqualByComparingTo("35.50");
        assertThat(price.currency()).isEqualTo("EUR");
    }

    @Test
    void shouldRejectStartDateAfterEndDate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(
                        1L,
                        END_DATE,
                        START_DATE,
                        1L,
                        35455L,
                        0,
                        new BigDecimal("35.50"),
                        "EUR"
                ))
                .withMessage("startDate no puede ser posterior a endDate");
    }

    @Test
    void shouldRejectNegativePrice() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(
                        1L,
                        START_DATE,
                        END_DATE,
                        1L,
                        35455L,
                        0,
                        new BigDecimal("-1.00"),
                        "EUR"
                ))
                .withMessage("El precio no puede ser negativo");
    }

    @Test
    void shouldRejectBlankCurrency() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(
                        1L,
                        START_DATE,
                        END_DATE,
                        1L,
                        35455L,
                        0,
                        new BigDecimal("35.50"),
                        " "
                ))
                .withMessage("currency no puede estar vacío");
    }
}