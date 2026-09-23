package com.dario.pricingservice.domain.port.input;

import com.dario.pricingservice.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

public interface FindApplicablePriceUseCase {

    Optional<Price> findApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    );
}
