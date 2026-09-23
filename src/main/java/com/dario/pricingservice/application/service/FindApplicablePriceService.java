package com.dario.pricingservice.application.service;

import com.dario.pricingservice.domain.model.Price;
import com.dario.pricingservice.domain.port.input.FindApplicablePriceUseCase;
import com.dario.pricingservice.domain.port.output.PriceQueryPort;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

public class FindApplicablePriceService
        implements FindApplicablePriceUseCase {

    private final PriceQueryPort priceQueryPort;

    public FindApplicablePriceService(PriceQueryPort priceQueryPort) {
        this.priceQueryPort = Objects.requireNonNull(
                priceQueryPort,
                "priceQueryPort no puede ser null"
        );
    }

    @Override
    public Optional<Price> findApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    ) {
        validateInput(applicationDate, productId, brandId);

        return priceQueryPort.findApplicablePrice(
                applicationDate,
                productId,
                brandId
        );
    }

    private void validateInput(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    ) {
        if (applicationDate == null) {
            throw new IllegalArgumentException(
                    "applicationDate no puede ser null"
            );
        }

        if (productId == null || productId <= 0) {
            throw new IllegalArgumentException(
                    "productId debe ser positivo"
            );
        }

        if (brandId == null || brandId <= 0) {
            throw new IllegalArgumentException(
                    "brandId debe ser positivo"
            );
        }
    }
}