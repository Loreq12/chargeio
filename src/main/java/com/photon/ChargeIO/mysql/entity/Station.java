package com.photon.ChargeIO.mysql.entity;

import javax.persistence.*;
import java.util.List;

@Entity
public class Station {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private PowerPlant powerPlant;
    private String name;
    private String country;
    private String city;
    private String zipCode;
    private String street;
    private String houseNumber;
    private Long capacity;
    private Long charge;

}
