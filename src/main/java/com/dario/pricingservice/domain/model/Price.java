package com.dario.pricingservice.domain.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Objects;

public record Price(
        Long brandId,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Long priceList,
        Long productId,
        Integer priority,
        BigDecimal price,
        String currency
) {

    public Price {
        Objects.requireNonNull(brandId, "brandId no puede ser null");
        Objects.requireNonNull(startDate, "startDate no puede ser null");
        Objects.requireNonNull(endDate, "endDate no puede ser null");
        Objects.requireNonNull(priceList, "priceList no puede ser null");
        Objects.requireNonNull(productId, "productId no puede ser null");
        Objects.requireNonNull(priority, "priority no puede ser null");
        Objects.requireNonNull(price, "price no puede ser null");
        Objects.requireNonNull(currency, "currency no puede ser null");

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "startDate no puede ser posterior a endDate"
            );
        }

        if (price.signum() < 0) {
            throw new IllegalArgumentException(
                    "El precio no puede ser negativo"
            );
        }

        if (currency.isBlank()) {
            throw new IllegalArgumentException(
                    "currency no puede estar vacío"
            );
        }
    }
}
