package com.photon.ChargeIO.controllers;

import com.photon.ChargeIO.mysql.entity.PowerPlant;
import com.photon.ChargeIO.mysql.repository.PowerPlantRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class PowerPlantApi {

    @Autowired
    private PowerPlantRepo ppRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating PowerPlant");

        HashMap<String, List<?>> data = new HashMap<>(){{
            put("name", Arrays.asList("Czarnobylanka"));
            put("maxEnergy", Arrays.asList(3000L));
            put("currentEnergy", Arrays.asList(2860L));
        }};

        for (int i = 0; i < data.get("name").size(); i++) {
            PowerPlant pp = new PowerPlant();
            pp.setName((String) data.get("name").get(i));
            pp.setMaxEnergy((Long) data.get("maxEnergy").get(i));
            pp.setCurrentEnergy((Long) data.get("currentEnergy").get(i));
            this.ppRepo.save(pp);
        }
        System.out.println("[MYSQL] PowerPlant genereted successfully");
    }

    @RequestMapping(value = "/powerplant/", method = RequestMethod.GET)
    public List<PowerPlant> listPowerPlants() {
        List<PowerPlant> l = new LinkedList<>();
        this.ppRepo.findAll().forEach(l::add);
        return l;
    }

}
