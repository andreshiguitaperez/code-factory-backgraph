package com.udea.graphvirtual.service;

import com.udea.graphvirtual.entity.Airport;
import com.udea.graphvirtual.entity.Flight;
import com.udea.graphvirtual.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.List;

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
                            String destination, LocalTime departureTime, LocalTime arrivalTime, Date departureDate, Date arrivalDate) {
        Airport origen = new Airport();
        origen.setId(UUID.fromString(origin));
        Airport destino = new Airport();
        destino.setId(UUID.fromString(destination));

        // Crear el objeto Flight con LocalTime para departureTime y arrivalTime
        Flight flight = Flight.builder()
                //.id(UUID.randomUUID()) // Si quieres generar un UUID automáticamente, descomenta esta línea
                .flightNumber(flightNumber)
                .seatsAvailable(seatsAvailable)
                .origin(origen)
                .destination(destino)
                .departureTime(departureTime) // LocalTime para hora de salida
                .arrivalTime(arrivalTime) // LocalTime para hora de llegada
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

        // Obtener los vuelos desde el repositorio
        List<Flight> flights = flightRepository.findByDepartureDateEqualsAndOriginEqualsAndDestinationEquals(
                departureDate, departureAirport, arrivalAirport);

        // Formatear las fechas de cada vuelo antes de devolverlas
        DateTimeFormatter timeFormat = DateTimeFormatter.ofPattern("HH:mm");
        for (Flight flight : flights) {
            // Convertir LocalTime a String para presentación
            if (flight.getDepartureTime() != null) {
                // Convertimos LocalTime a String
                String departureTimeString = flight.getDepartureTime().format(timeFormat);
                flight.setDepartureTimeString(departureTimeString);  // Asumiendo que tienes un campo String para almacenar la hora en formato de texto
            }
            if (flight.getArrivalTime() != null) {
                // Convertimos LocalTime a String
                String arrivalTimeString = flight.getArrivalTime().format(timeFormat);
                flight.setArrivalTimeString(arrivalTimeString);  // Lo mismo aquí
            }
        }

        return flights;
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
