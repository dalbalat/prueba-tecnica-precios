package com.dario.pricingservice.application.port.out;

import com.dario.pricingservice.domain.model.Price;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Puerto de salida para consultar la tarifa aplicable.
 *
 * Define el comportamiento requerido por la aplicación,
 * sin exponer detalles de persistencia.
 */
public interface PriceQueryPort {

    /**
     * Obtiene una tarifa para el producto, la marca y la fecha indicados.
     *
     * Solo considera tarifas cuyo intervalo de vigencia contiene
     * applicationDate, incluyendo las fechas de inicio y fin.
     *
     * Selecciona la tarifa con mayor prioridad. A igualdad de prioridad,
     * selecciona la de menor priceList.
     *
     * Si varias tarifas coinciden también en priceList, este contrato
     * no establece un criterio adicional entre ellas.
     *
     * @param applicationDate fecha y hora de aplicación, no nula
     * @param productId identificador positivo del producto
     * @param brandId identificador positivo de la marca
     * @return una tarifa que cumple los criterios de selección,
     *         o un Optional vacío si no existe ninguna aplicable
     */
    Optional<Price> findApplicablePrice(
            LocalDateTime applicationDate,
            Long productId,
            Long brandId
    );
}
