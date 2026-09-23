package com.dario.pricingservice.adapter.output.persistence.repository;

import com.dario.pricingservice.adapter.output.persistence.entity.PriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface PriceJpaRepository
        extends JpaRepository<PriceEntity, Long> {

    Optional<PriceEntity>
    findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescPriceListAsc(
            Long productId,
            Long brandId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );
}
