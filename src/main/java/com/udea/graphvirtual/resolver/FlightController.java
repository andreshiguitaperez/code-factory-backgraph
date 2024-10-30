package com.udea.graphvirtual.resolver;

import com.udea.graphvirtual.entity.Flight;
import com.udea.graphvirtual.service.FlightService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@Controller
public class FlightController {

    private final FlightService flightService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

    public FlightController(FlightService flightService) {
        this.flightService = flightService;
    }

    @QueryMapping
    public List<Flight> allFlights() {
        return flightService.getAllFlights();
    }

    @QueryMapping
    public Flight flightById(@Argument Long id) {
        return flightService.getFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));
    }


    @MutationMapping
    public Flight addFlight(@Argument String flightNumber, @Argument int seatsAvailable, @Argument String origin, @Argument String destination,
                            @Argument String departureTime, @Argument String arrivalTime) {
        try {
            LocalDateTime departure = LocalDateTime.parse(departureTime, DATE_FORMATTER);
            LocalDateTime arrival = LocalDateTime.parse(arrivalTime, DATE_FORMATTER);
            return flightService.addFlight(flightNumber, seatsAvailable, origin, destination, departure, arrival);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format: yyyy-MM-dd'T'HH:mm:ss", e);
        }
    }

    @QueryMapping
    public List<Flight> searchFlights(@Argument String origin, @Argument String destination, @Argument String departureDate) {
        try {
            LocalDateTime departureDateTime = LocalDateTime.parse(departureDate + "T00:00:00", DATE_FORMATTER);
            return flightService.searchFlights(origin, destination, departureDateTime);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format: yyyy-MM-dd", e);
        }
    }

    @QueryMapping
    public Flight getFlightDetails(@Argument Long flightId) {
        return flightService.getFlightById(flightId) // Esto devuelve un Optional<Flight>
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + flightId));
    }



}
