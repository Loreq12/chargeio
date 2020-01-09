package com.photon.ChargeIO.mysql.entity;

import javax.persistence.*;

@Entity
public class Price {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private ChargeType chargeType;
    private Double price;

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", chargeType=" + chargeType +
                ", price=" + price +
                '}';
    }

    public ChargeType getChargeType() {
        return chargeType;
    }

    public void setChargeType(ChargeType chargeType) {
        this.chargeType = chargeType;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
