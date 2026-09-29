package com.dario.pricingservice.adapter.output.persistence.repository;

import com.dario.pricingservice.adapter.output.persistence.entity.PriceEntity;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Test
    @Transactional
    void shouldSelectLowestPriceListWhenPrioritiesAreEqual() {
        LocalDateTime startDate = LocalDateTime.of(2020, 6, 14, 0, 0);
        LocalDateTime endDate = LocalDateTime.of(2020, 6, 14, 23, 59, 59);
        LocalDateTime applicationDate = LocalDateTime.of(2020, 6, 14, 16, 0);

        Long productId = 90001L;
        Long brandId = 90001L;

        PriceEntity higherPriceList = PriceEntity.of(
                brandId,
                startDate,
                endDate,
                20L,
                productId,
                1,
                new BigDecimal("25.00"),
                "EUR"
        );

        PriceEntity lowerPriceList = PriceEntity.of(
                brandId,
                startDate,
                endDate,
                10L,
                productId,
                1,
                new BigDecimal("40.00"),
                "EUR"
        );

        priceJpaRepository.saveAndFlush(higherPriceList);
        priceJpaRepository.saveAndFlush(lowerPriceList);

        Optional<PriceEntity> result = priceJpaRepository
                .findFirstByProductIdAndBrandIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDescPriceListAsc(
                        productId,
                        brandId,
                        applicationDate,
                        applicationDate
                );

        assertThat(result).isPresent();

        PriceEntity selectedPrice = result.orElseThrow();

        assertThat(selectedPrice.getId()).isEqualTo(lowerPriceList.getId());
        assertThat(selectedPrice.getPriceList()).isEqualTo(10L);
        assertThat(selectedPrice.getPriority()).isEqualTo(1);
        assertThat(selectedPrice.getPrice()).isEqualByComparingTo("40.00");
    }
}
