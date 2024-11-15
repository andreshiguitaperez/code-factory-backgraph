package com.udea.graphvirtual.repository;

import com.udea.graphvirtual.entity.Airport;
import com.udea.graphvirtual.entity.Flight;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Repository
public interface FlightRepository extends JpaRepository<Flight, UUID> {
    // Método para buscar vuelos por origen, destino y tiempo de salida
    //List<Flight> findByOriginAndDestinationAndDepartureTimeBetween(String origin, String destination, LocalDateTime startDateTime, LocalDateTime endDateTime);

    // Método para buscar vuelos por origen, destino, tiempo de salida y rango de precios
   /* List<Flight> findByOriginAndDestinationAndDepartureTimeBetweenAndPriceBetween(
            String origin,
            String destination,
            LocalDateTime startDateTime,
            LocalDateTime endDateTime,
            Float minPrice,
            Float maxPrice
    ); */

    List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEquals(Date departureTime, Airport departureAirport, Airport arrivalAirport);

    List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndDepartureTimeBetween(Date departureDate, Airport departureAirport, Airport arrivalAirport, Calendar departureTime, Calendar finalDepartureTime);

    List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndDepartureTimeEquals(Date departureDate, Airport departureAirport, Airport arrivalAirport, Calendar departureTime);

    List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndArrivalTimeBetween(Date departureDate, Airport departureAirport, Airport arrivalAirport, Calendar arrivalTime, Calendar finalArrivalTime);

    List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndArrivalTimeEquals(Date departureDate, Airport departureAirport, Airport arrivalAirport, Calendar arrivalTime);
}
