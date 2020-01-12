package com.photon.ChargeIO.mysql.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class TransactionHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private Wallet wallet;
    private Date date;
    private double ammont_change;

    @Override
    public String toString() {
        return "TransactionHistory{" +
                "id=" + id +
                ", wallet=" + wallet +
                ", date=" + date +
                '}';
    }

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getAmmont_change() {
        return ammont_change;
    }

    public void setAmmont_change(double ammont_change) {
        this.ammont_change = ammont_change;
    }
}
