package com.dario.pricingservice.domain.port.output;

import com.dario.pricingservice.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceQueryPort {

    Optional<Price> findApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    );
}
