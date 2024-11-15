package com.udea.graphvirtual.resolver;

import com.udea.graphvirtual.entity.Airport;
import com.udea.graphvirtual.entity.Flight;
import com.udea.graphvirtual.service.FlightService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@CrossOrigin(origins = "*")

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
    public Flight flightById(@Argument UUID id) {
        return flightService.getFlightById(id)
                .orElseThrow(() -> new RuntimeException("Flight not found with ID: " + id));
    }


    @MutationMapping
    public Flight addFlight(@Argument UUID id, @Argument String flightNumber, @Argument int seatsAvailable, @Argument String origin, @Argument String destination,
                            @Argument String departureTime, @Argument String arrivalTime, @Argument Double price, @Argument Double distance, @Argument String departureDate, @Argument String arrivalDate ) {
        try {
            Calendar departure = Calendar.getInstance();
            departure.setTime(new SimpleDateFormat("HH:mm:ss").parse(departureTime));
            Calendar arrival = Calendar.getInstance();
            arrival.setTime(new SimpleDateFormat("HH:mm:ss").parse(arrivalTime));

            return flightService.addFlight(flightNumber, seatsAvailable, origin, price, distance, destination, departure, arrival, new SimpleDateFormat("yyyy-MM-dd").parse(departureDate), new SimpleDateFormat("yyyy-MM-dd").parse(arrivalDate));
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format: yyyy-MM-dd'T'HH:mm:ss", e);
        }
    }

    /*@QueryMapping
    public List<Flight> searchFlights(@Argument String origin, @Argument String destination,
                                      @Argument String departureDate,
                                      @Argument Float minPrice, @Argument Float maxPrice) {
        try {
            // Cambia el formato del departureDate para que sea LocalDate
            LocalDate departureLocalDate = LocalDate.parse(departureDate); // Suponiendo que llega en formato "yyyy-MM-dd"

            // Llama al servicio con los parámetros de precio
            return flightService.searchFlights(origin, destination, departureLocalDate, minPrice, maxPrice);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use the format: yyyy-MM-dd", e);
        }
    } */

    @QueryMapping
    public List<Flight> findFlightsByOriginDestinationDate(
            @Argument String departureDate,
            @Argument UUID origin,
            @Argument UUID destination
    ) throws ParseException {
        Airport departureAirport = new Airport();
        Airport arrivalAirport = new Airport();
        departureAirport.setId(origin);
        arrivalAirport.setId(destination);
        return flightService.findByDepartureDateEqualsAndOriginEqualsAndDestinationEquals(
                new SimpleDateFormat("yyyy-MM-dd").parse(departureDate),
                departureAirport,
                arrivalAirport);
    }

    @QueryMapping
    public List<Flight> findFlightsByOriginDestinationDateFilteredByDepartureTime(
            @Argument String departureDate,
            @Argument UUID origin,
            @Argument UUID destination,
            @Argument String departureTime,
            @Argument String finalDepartureTime
    ) throws ParseException {
        Airport departureAirport = new Airport();
        Airport arrivalAirport = new Airport();
        departureAirport.setId(origin);
        arrivalAirport.setId(destination);
        return flightService.findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndDepartureTimeRange(
                new SimpleDateFormat("yyyy-MM-dd").parse(departureDate),
                departureAirport,
                arrivalAirport,
                departureTime,
                finalDepartureTime);
    }

    @QueryMapping
    public List<Flight> findFlightsByOriginDestinationDateFilteredByArrivalTime(
            @Argument String departureDate,
            @Argument UUID origin,
            @Argument UUID destination,
            @Argument String arrivalTime,
            @Argument String finalArrivalTime
    ) throws ParseException {
        Airport departureAirport = new Airport();
        Airport arrivalAirport = new Airport();
        departureAirport.setId(origin);
        arrivalAirport.setId(destination);
        return flightService.findByDepartureDateEqualsAndOriginEqualsAndDestinationEqualsAndArrivalTimeRange(
                new SimpleDateFormat("yyyy-MM-dd").parse(departureDate),
                departureAirport,
                arrivalAirport,
                arrivalTime,
                finalArrivalTime);
    }


}
