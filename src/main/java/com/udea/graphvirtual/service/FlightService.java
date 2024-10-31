package com.udea.graphvirtual.service;

import com.udea.graphvirtual.entity.Flight;
import com.udea.graphvirtual.repository.FlightRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class FlightService {

    private final FlightRepository flightRepository;

    public FlightService(FlightRepository flightRepository) {
        this.flightRepository = flightRepository;
    }

    public List<Flight> getAllFlights(){
        return flightRepository.findAll();
    }

    public Optional<Flight> getFlightById(Long id) {
        return flightRepository.findById(id); // Esto devuelve Optional<Flight>
    }



    public Flight addFlight(String flightNumber, int seatsAvailable, String origin,
                            String destination, LocalDateTime departureTime, LocalDateTime arrivalTime){
        Flight flight = Flight.builder()
                .flightNumber(flightNumber)
                .seatsAvailable(seatsAvailable)
                .origin(origin)
                .destination(destination)
                .departureTime(departureTime)
                .arrivalTime(arrivalTime)
                .build();

        return flightRepository.save(flight);
    }

    public List<Flight> searchFlights(String origin, String destination, LocalDate departureDate) {
        // Modificar esta lógica según cómo quieras manejar la búsqueda con fecha
        LocalDateTime startDateTime = departureDate.atStartOfDay(); // Comienza desde la medianoche de esa fecha
        LocalDateTime endDateTime = departureDate.plusDays(1).atStartOfDay(); // Hasta la medianoche del día siguiente

        return flightRepository.findByOriginAndDestinationAndDepartureTimeBetween(origin, destination, startDateTime, endDateTime);
    }


}
