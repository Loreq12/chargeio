package com.photon.ChargeIO.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.ChargeIO.mongo.document.Point;
import com.photon.ChargeIO.mongo.repository.PointRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
public class PointApi {

    @Autowired
    private PointRepo repo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MONGO] Preparing data");
        this.repo.deleteAll();
        System.out.println("[MONGO] Data cleared");

        System.out.println("[MONGO] Generating new geo location data");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map<String, Object> pointsList = objectMapper.readValue(
                    new File(ServletContextListener.class.getClassLoader().getResource("city.json").getPath()),
                    new TypeReference<Map<String, Object>>(){});
            for (Map.Entry<String, Object> _i: pointsList.entrySet()) {
                Point p = new Point();
                p.setName(_i.getKey());
                p.setNeighbours(((Map<String, List<Integer>>)_i.getValue()).get("neighbours"));
                List<Double> qq = ((Map<String, List<Double>>)_i.getValue()).get("position");
                p.setPosition(new GeoJsonPoint(
                        qq.get(0),
                        qq.get(1)
                ));
                this.repo.save(p);
            }
            System.out.println("[MONGO] Geo locations genereted successfully");
        } catch (IOException e) {
            System.out.println("[MONGO] Error has occured during data generation");
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/points/", method = RequestMethod.GET)
    public List<Point> listAllPoints() {
        return this.repo.findAll();
    }
}
