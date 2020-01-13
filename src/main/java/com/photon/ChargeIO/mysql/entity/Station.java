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
    private Long pointNumber;
    private Boolean occupied;
    private Boolean service;
    @OneToOne
    private ChargeType type;

    @OneToOne
    private User occupiedBy = null;

    @Override
    public String toString() {
        return "Station{" +
                "id=" + id +
                ", powerPlant=" + powerPlant +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", street='" + street + '\'' +
                ", houseNumber='" + houseNumber + '\'' +
                ", capacity=" + capacity +
                ", charge=" + charge +
                ", pointNumber=" + pointNumber +
                ", occupied=" + occupied +
                '}';
    }

    public Boolean getService() {
        return service;
    }
    
    public void setService(Boolean service) {
        this.service = service;
    }

    public PowerPlant getPowerPlant() {
        return powerPlant;
    }

    public void setPowerPlant(PowerPlant powerPlant) {
        this.powerPlant = powerPlant;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public void setHouseNumber(String houseNumber) {
        this.houseNumber = houseNumber;
    }

    public Long getCapacity() {
        return capacity;
    }

    public void setCapacity(Long capacity) {
        this.capacity = capacity;
    }

    public Long getCharge() {
        return charge;
    }

    public void setCharge(Long charge) {
        this.charge = charge;
    }

    public Long getPointNumber() {
        return pointNumber;
    }

    public void setPointNumber(Long pointNumber) {
        this.pointNumber = pointNumber;
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
    }

    public User getOccupiedBy() {
        return occupiedBy;
    }

    public void setOccupiedBy(User occupiedBy) {
        this.occupiedBy = occupiedBy;
    }

    public ChargeType getType() {
        return type;
    }

    public void setType(ChargeType type) {
        this.type = type;
    }
}
