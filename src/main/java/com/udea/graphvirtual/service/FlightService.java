package com.udea.graphvirtual.service;

import com.udea.graphvirtual.entity.Airport;
import com.udea.graphvirtual.entity.Flight;
import com.udea.graphvirtual.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights() {
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(UUID id) {
        return flightRepository.findById(id); // Esto devuelve Optional<Flight>
    }

    public Flight addFlight(String flightNumber, int seatsAvailable, String origin, Double price, Double distance,
                            String destination, Calendar departureTime, Calendar arrivalTime, Date departureDate, Date arrivalDate) {
        Airport origen = new Airport();
        origen.setId(UUID.fromString(origin));
        Airport destino = new Airport();
        destino.setId(UUID.fromString(destination));
        Flight flight = Flight.builder()
                //.id(UUID.randomUUID())
                .flightNumber(flightNumber)
                .seatsAvailable(seatsAvailable)
                .origin(origen)
                .destination(destino)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .departureDate(departureDate)
                .arrivalDate(arrivalDate)
                .price(price)
                .distance(distance)
                .build();

        return flightRepository.save(flight);
    }

    // Actualizar el método searchFlights para incluir el filtrado por precio
    /*public List<Flight> searchFlights(String origin, String destination, LocalDate departureDate,
                                      Float minPrice, Float maxPrice) {
        LocalDateTime startDateTime = departureDate.atStartOfDay(); // Comienza desde la medianoche de esa fecha
        LocalDateTime endDateTime = departureDate.plusDays(1).atStartOfDay(); // Hasta la medianoche del día siguiente

        // Lógica de búsqueda
        if (minPrice != null && maxPrice != null) {
            return flightRepository.findByOriginAndDestinationAndDepartureTimeBetweenAndPriceBetween(
                    origin, destination, startDateTime, endDateTime, minPrice, maxPrice);
        } else {
            return flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(
                    origin, destination, startDateTime, endDateTime);
        }
    } */

    public List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEquals(
            Date departureDate,
            Airport departureAirport,
            Airport arrivalAirport) {
        return flightRepository.findByDepartureDateEqualsAndOriginEqualsAndDestinationEquals(departureDate, departureAirport, arrivalAirport);
    }

    public List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndDepartureTimeRange(
            Date departureDate,
            Airport origin,
            Airport destination,
            String departureTime,
            String finalDepartureTime
    ) throws ParseException {
        Calendar initialTime = Calendar.getInstance();
        initialTime.setTime(new SimpleDateFormat("HH:mm:ss").parse(departureTime));
        if (finalDepartureTime != null) {
            Calendar finalTime = Calendar.getInstance();
            finalTime.setTime(new SimpleDateFormat("HH:mm:ss").parse(finalDepartureTime));
            return flightRepository.findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndDepartureTimeBetween(departureDate, origin, destination, initialTime, finalTime);
        }
        else{
            return flightRepository.findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndDepartureTimeEquals(departureDate, origin, destination, initialTime);
        }
    }

    public List<Flight> findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndArrivalTimeRange(
            Date departureDate,
            Airport origin,
            Airport destination,
            String arrivalTime,
            String finalArrivalTime
    ) throws ParseException {
        Calendar initialTime = Calendar.getInstance();
        initialTime.setTime(new SimpleDateFormat("HH:mm:ss").parse(arrivalTime));
        if (finalArrivalTime != null) {
            Calendar finalTime = Calendar.getInstance();
            finalTime.setTime(new SimpleDateFormat("HH:mm:ss").parse(finalArrivalTime));
            return flightRepository.findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndArrivalTimeBetween(departureDate, origin, destination, initialTime, finalTime);
        }
        else{
            return flightRepository.findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndArrivalTimeEquals(departureDate, origin, destination, initialTime);
        }
    }
}
