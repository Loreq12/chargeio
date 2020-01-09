package com.photon.ChargeIO.mysql.entity;

import javax.persistence.*;
import java.sql.Date;

@Entity
public class PowerPlantHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private Date creationDate;
    private Integer energy;
    @ManyToOne
    private PowerPlant powerPlant;

}
