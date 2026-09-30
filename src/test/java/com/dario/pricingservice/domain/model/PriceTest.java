package com.dario.pricingservice.domain.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;

class PriceTest {

    private static final Long BRAND_ID = 1L;
    private static final LocalDateTime START_DATE =
            LocalDateTime.of(2020, 6, 14, 0, 0);
    private static final LocalDateTime END_DATE =
            LocalDateTime.of(2020, 12, 31, 23, 59, 59);
    private static final Long PRICE_LIST = 1L;
    private static final Long PRODUCT_ID = 35455L;
    private static final Integer PRIORITY = 0;
    private static final BigDecimal PRICE = new BigDecimal("35.50");
    private static final String CURRENCY = "EUR";

    private static Price validPrice() {
        return new Price(
                BRAND_ID,
                START_DATE,
                END_DATE,
                PRICE_LIST,
                PRODUCT_ID,
                PRIORITY,
                PRICE,
                CURRENCY
        );
    }

    @Test
    void shouldCreateValidPrice() {
        Price price = validPrice();

        assertThat(price.brandId()).isEqualTo(BRAND_ID);
        assertThat(price.startDate()).isEqualTo(START_DATE);
        assertThat(price.endDate()).isEqualTo(END_DATE);
        assertThat(price.priceList()).isEqualTo(PRICE_LIST);
        assertThat(price.productId()).isEqualTo(PRODUCT_ID);
        assertThat(price.priority()).isEqualTo(PRIORITY);
        assertThat(price.price()).isEqualByComparingTo(PRICE);
        assertThat(price.currency()).isEqualTo(CURRENCY);
    }

    @Test
    void shouldAllowStartDateEqualToEndDate() {
        Price price = new Price(
                BRAND_ID,
                START_DATE,
                START_DATE,
                PRICE_LIST,
                PRODUCT_ID,
                PRIORITY,
                PRICE,
                CURRENCY
        );

        assertThat(price.startDate()).isEqualTo(price.endDate());
    }

    @Test
    void shouldAllowZeroPrice() {
        Price price = new Price(
                BRAND_ID,
                START_DATE,
                END_DATE,
                PRICE_LIST,
                PRODUCT_ID,
                PRIORITY,
                BigDecimal.ZERO,
                CURRENCY
        );

        assertThat(price.price()).isEqualByComparingTo(BigDecimal.ZERO);
    }

    @Test
    void shouldRejectStartDateAfterEndDate() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        END_DATE,
                        START_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("startDate no puede ser posterior a endDate");
    }

    @Test
    void shouldRejectNegativePrice() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        new BigDecimal("-1.00"),
                        CURRENCY
                ))
                .withMessage("El precio no puede ser negativo");
    }

    @Test
    void shouldRejectBlankCurrency() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        " "
                ))
                .withMessage("currency no puede estar vacío");
    }

    @Test
    void shouldRejectNullBrandId() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        null,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("brandId no puede ser null");
    }

    @Test
    void shouldRejectNullStartDate() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        null,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("startDate no puede ser null");
    }

    @Test
    void shouldRejectNullEndDate() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        null,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("endDate no puede ser null");
    }

    @Test
    void shouldRejectNullPriceList() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        null,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("priceList no puede ser null");
    }

    @Test
    void shouldRejectNullProductId() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        null,
                        PRIORITY,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("productId no puede ser null");
    }

    @Test
    void shouldRejectNullPriority() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        null,
                        PRICE,
                        CURRENCY
                ))
                .withMessage("priority no puede ser null");
    }

    @Test
    void shouldRejectNullPrice() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        null,
                        CURRENCY
                ))
                .withMessage("price no puede ser null");
    }

    @Test
    void shouldRejectNullCurrency() {
        assertThatNullPointerException()
                .isThrownBy(() -> new Price(
                        BRAND_ID,
                        START_DATE,
                        END_DATE,
                        PRICE_LIST,
                        PRODUCT_ID,
                        PRIORITY,
                        PRICE,
                        null
                ))
                .withMessage("currency no puede ser null");
    }
}