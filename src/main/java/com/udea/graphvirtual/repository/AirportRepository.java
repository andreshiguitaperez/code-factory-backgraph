package com.udea.graphvirtual.repository;

import com.udea.graphvirtual.entity.Airport;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface AirportRepository extends JpaRepository<Airport, UUID> {
}
