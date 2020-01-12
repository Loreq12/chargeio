package com.photon.ChargeIO.controllers;

import com.photon.ChargeIO.mysql.entity.*;
import com.photon.ChargeIO.mysql.repository.PowerPlantRepo;
import com.photon.ChargeIO.mysql.repository.StationRepo;
import com.photon.ChargeIO.mysql.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.function.Consumer;

@RestController
public class StationApi {

    @Autowired
    private StationRepo sRepo;

    @Autowired
    private PowerPlantRepo ppRepo;

    @Autowired
    private UserRepo uRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating statioins");
        HashMap<String, List<?>> data = new HashMap<>() {{
            put("powerPlant", Arrays.asList(ppRepo.findByName("Czarnobylanka"), ppRepo.findByName("Czarnobylanka"), ppRepo.findByName("Czarnobylanka")));
            put("name", Arrays.asList("N1", "N2", "N3"));
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

            Price price = new Price();
            price.setPrice(1.1);
            ChargeType type = new ChargeType();
            type.setName("normal");
            type.setPrice(price);
            s.setType(type);

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

    @RequestMapping(value = "/bookstation/", method = RequestMethod.POST)
    public String bookStation(@RequestParam("station") String station, @RequestParam("email") String email) {
        System.out.println("szukam stacji: "+ station);
        User u = this.uRepo.findByEmail(email);
        if (u == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        Station s = sRepo.findByName(station);
        if (s == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Station not found");

        if (s.getOccupied()) return "Station already occupied";
        s.setOccupied(true);
        s.setOccupiedBy(u);

        sRepo.save(s);
        return "Booking successful";
    }

    @RequestMapping(value = "/unbookstation/", method = RequestMethod.POST)
    public String unbookStation(@RequestParam("station") String name) {
        Station s = sRepo.findByName(name);
        if (s == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Station not found");

        s.setOccupied(false);
        s.setOccupiedBy(null);

        sRepo.save(s);
        return "unbooked";
    }
}
