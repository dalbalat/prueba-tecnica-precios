package com.dario.pricingservice.adapter.output.persistence.repository;

import com.dario.pricingservice.adapter.output.persistence.entity.PriceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class PriceJpaRepositoryTest {

    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Test
    void shouldFindHighestPriorityApplicablePrice() {
        LocalDateTime applicationDate =
                LocalDateTime.of(2020, 6, 14, 16, 0);

        Optional<PriceEntity> result =
                priceJpaRepository
                        .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescPriceListAsc(
                                35455L,
                                1L,
                                applicationDate,
                                applicationDate
                        );

        assertThat(result).isPresent();

        PriceEntity price = result.orElseThrow();

        assertThat(price.getPriceList()).isEqualTo(2L);
        assertThat(price.getPrice()).isEqualByComparingTo("25.45");
        assertThat(price.getPriority()).isEqualTo(1);
    }

    @Test
    void shouldReturnEmptyWhenThereIsNoApplicablePrice() {
        LocalDateTime applicationDate =
                LocalDateTime.of(2021, 1, 1, 0, 0);

        Optional<PriceEntity> result =
                priceJpaRepository
                        .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescPriceListAsc(
                                35455L,
                                1L,
                                applicationDate,
                                applicationDate
                        );

        assertThat(result).isEmpty();
    }
}