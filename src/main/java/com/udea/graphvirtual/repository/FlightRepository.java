package com.udea.graphvirtual.repository;

import com.udea.graphvirtual.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long> {
    // Método para buscar vuelos por origen, destino y tiempo de salida
    List<Flight> findByOriginAndDestinationAndDepartureTimeBetween(String origin, String destination, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Método para buscar vuelos por origen, destino, tiempo de salida y rango de precios
    List<Flight> findByOriginAndDestinationAndDepartureTimeBetweenAndPriceBetween(
            String origin,
            String destination,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Float minPrice,
            Float maxPrice
    );
}
