package com.dario.pricingservice.adapter.output.persistence.adapter;

import com.dario.pricingservice.adapter.output.persistence.mapper.PricePersistenceMapper;
import com.dario.pricingservice.adapter.output.persistence.repository.PriceJpaRepository;
import com.dario.pricingservice.domain.model.Price;
import com.dario.pricingservice.domain.port.output.PriceQueryPort;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class PricePersistenceAdapter implements PriceQueryPort {

    private final PriceJpaRepository priceJpaRepository;
    private final PricePersistenceMapper pricePersistenceMapper;

    public PricePersistenceAdapter(
            PriceJpaRepository priceJpaRepository,
            PricePersistenceMapper pricePersistenceMapper
    ) {
        this.priceJpaRepository = priceJpaRepository;
        this.pricePersistenceMapper = pricePersistenceMapper;
    }

    @Override
    public Optional<Price> findApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    ) {
        return priceJpaRepository
                .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescPriceListAsc(
                        productId,
                        brandId,
                        applicationDate,
                        applicationDate
                )
                .map(pricePersistenceMapper::toDomain);
    }
}
