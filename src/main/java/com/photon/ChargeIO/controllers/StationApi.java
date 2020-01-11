package com.photon.ChargeIO.controllers;

import com.photon.ChargeIO.mysql.entity.PowerPlant;
import com.photon.ChargeIO.mysql.entity.Station;
import com.photon.ChargeIO.mysql.entity.User;
import com.photon.ChargeIO.mysql.repository.PowerPlantRepo;
import com.photon.ChargeIO.mysql.repository.StationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

@RestController
public class StationApi {

    @Autowired
    private StationRepo sRepo;

    @Autowired
    private PowerPlantRepo ppRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating statioins");
        HashMap<String, List<?>> data = new HashMap<>(){{
            put("powerPlant", Arrays.asList(ppRepo.findByName("Czarnobylanka"), ppRepo.findByName("Czarnobylanka"), ppRepo.findByName("Czarnobylanka")));
            put("name", Arrays.asList("#1", "#2", "#3"));
            put("country", Arrays.asList("Poland", "Poland", "Poland"));
            put("city", Arrays.asList("Kraków", "Kraków", "Kraków"));
            put("zipCode", Arrays.asList("31469", "31469", "31469"));
            put("street", Arrays.asList("Wadowicka", "Słomiana", "Warszawska"));
            put("houseNumber", Arrays.asList("2", "14", "25"));
            put("capacity", Arrays.asList(300L, 270L, 440L));
            put("charge", Arrays.asList(300L, 270L, 440L));
            put("pointNumber", Arrays.asList(399L, 464L, 196L));
            put("occupied", Arrays.asList(false, false, false));
        }};

        for (int i = 0; i < data.get("name").size(); i++) {
            Station s = new Station();
            s.setPowerPlant((PowerPlant) data.get("powerPlant").get(i));
            s.setName((String) data.get("name").get(i));
            s.setCountry((String) data.get("country").get(i));
            s.setCity((String) data.get("city").get(i));
            s.setZipCode((String) data.get("zipCode").get(i));
            s.setStreet((String) data.get("street").get(i));
            s.setHouseNumber((String) data.get("houseNumber").get(i));
            s.setCapacity((Long) data.get("capacity").get(i));
            s.setCharge((Long) data.get("charge").get(i));
            s.setPointNumber((Long) data.get("pointNumber").get(i));
            s.setOccupied((Boolean) data.get("occupied").get(i));
            this.sRepo.save(s);
        }
        System.out.println("[MYSQL] Stations genereted successfully");
    }

    @RequestMapping(value = "/stations/", method = RequestMethod.GET)
    public List<Station> listPowerPlants() {
        List<Station> l = new LinkedList<>();
        this.sRepo.findAll().forEach(l::add);
        return l;
    }

}
