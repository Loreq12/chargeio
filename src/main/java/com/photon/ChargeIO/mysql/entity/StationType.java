package com.photon.ChargeIO.mysql.entity;

import javax.persistence.*;

@Entity
public class StationType {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Station station;
    @ManyToOne
    private ChargeType chargeType;
    private Integer quantity;

    @Override
    public String toString() {
        return "StationType{" +
                "station=" + station +
                ", chargeType=" + chargeType +
                ", quantity=" + quantity +
                '}';
    }

    public Station getStation() {
        return station;
    }

    public void setStation(Station station) {
        this.station = station;
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
