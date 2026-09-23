package com.dario.pricingservice.adapter.input.rest;

import com.dario.pricingservice.adapter.input.rest.dto.PriceResponse;
import com.dario.pricingservice.domain.model.Price;
import org.springframework.stereotype.Component;

@Component
public class PriceResponseMapper {

    public PriceResponse toResponse(Price price) {
        return new PriceResponse(
                price.productId(),
                price.brandId(),
                price.priceList(),
                price.startDate(),
                price.endDate(),
                price.price(),
                price.currency()
        );
    }
}
