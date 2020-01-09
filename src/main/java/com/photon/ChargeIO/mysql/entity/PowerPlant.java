package com.photon.ChargeIO.mysql.entity;

import javax.persistence.*;

@Entity
public class PowerPlant {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column(unique = true)
    private String name;
    private Long maxEnergy;
    private Long currentEnergy;

    @Override
    public String toString() {
        return "PowerPlant{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", maxEnergy=" + maxEnergy +
                ", currentEnergy=" + currentEnergy +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getMaxEnergy() {
        return maxEnergy;
    }

    public void setMaxEnergy(Long maxEnergy) {
        this.maxEnergy = maxEnergy;
    }

    public Long getCurrentEnergy() {
        return currentEnergy;
    }

    public void setCurrentEnergy(Long currentEnergy) {
        this.currentEnergy = currentEnergy;
    }
}
