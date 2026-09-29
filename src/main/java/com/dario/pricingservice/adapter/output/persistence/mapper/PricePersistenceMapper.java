package com.dario.pricingservice.adapter.output.persistence.mapper;

import com.dario.pricingservice.adapter.output.persistence.entity.PriceEntity;
import com.dario.pricingservice.domain.model.Price;

public class PricePersistenceMapper {

    public Price toDomain(PriceEntity entity) {
        return new Price(
                entity.getBrandId(),
                entity.getStartDate(),
                entity.getEndDate(),
                entity.getPriceList(),
                entity.getProductId(),
                entity.getPriority(),
                entity.getPrice(),
                entity.getCurrency()
        );
    }

}
