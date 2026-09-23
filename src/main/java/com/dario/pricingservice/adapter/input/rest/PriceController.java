package com.dario.pricingservice.adapter.input.rest;

import com.dario.pricingservice.adapter.input.rest.dto.PriceResponse;
import com.dario.pricingservice.domain.port.input.FindApplicablePriceUseCase;
import jakarta.validation.constraints.Positive;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/v1/prices")
@Validated
public class PriceController {

    private final FindApplicablePriceUseCase findApplicablePriceUseCase;
    private final PriceResponseMapper priceResponseMapper;

    public PriceController(
            FindApplicablePriceUseCase findApplicablePriceUseCase,
            PriceResponseMapper priceResponseMapper
    ) {
        this.findApplicablePriceUseCase = findApplicablePriceUseCase;
        this.priceResponseMapper = priceResponseMapper;
    }

    @GetMapping
    public ResponseEntity<PriceResponse> findApplicablePrice(
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime applicationDate,

            @RequestParam
            @Positive
            Long productId,

            @RequestParam
            @Positive
            Long brandId
    ) {
        return findApplicablePriceUseCase
                .findApplicablePrice(
                        applicationDate,
                        productId,
                        brandId
                )
                .map(priceResponseMapper::toResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}