package com.udea.graphvirtual.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Airplane {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String model;

    private int capacity;

    @ManyToOne
    @JoinColumn(name = "airline_id", nullable = false)
    private Airline airline;
}