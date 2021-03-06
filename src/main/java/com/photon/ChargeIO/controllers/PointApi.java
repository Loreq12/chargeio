package com.photon.ChargeIO.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.photon.ChargeIO.misc.Dijkstra;
import com.photon.ChargeIO.misc.DijkstraNode;
import com.photon.ChargeIO.misc.NodePreparator;
import com.photon.ChargeIO.mongo.document.Point;
import com.photon.ChargeIO.mongo.repository.PointRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletContextListener;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class PointApi {

    @Autowired
    private PointRepo repo;

    private DijkstraNode [] nodes;

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
                p.setPosition(new org.springframework.data.geo.Point(
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

        //Sort them all
        List <Point> lista = this.repo.findAll();
        try {
            NodePreparator preparator = new NodePreparator(lista);
            nodes = preparator.prepare();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @RequestMapping(value = "/point/nearest/", method = RequestMethod.GET)
    public Point getNearestPoint( @RequestParam("x") double x, @RequestParam("y") double y) {
        return this.repo.findTop1ByPositionNear(new org.springframework.data.geo.Point(x, y));
    }

    @RequestMapping(value = "/point/all/", method = RequestMethod.GET)
    public HashMap<String, List<Point>> listAllPoints() {
        return new HashMap<String, List<Point>>(){{
            put("points", repo.findAll());
        }};
    }

    @RequestMapping(value = "/dijkstra/", method = RequestMethod.GET)
    public HashMap<String, List<Integer>> searchPath(@RequestParam("begin") int begin, @RequestParam("end") int end) {
        Dijkstra dijkstra = new Dijkstra(nodes);
        return new HashMap<String, List<Integer>>(){{
            put("path", dijkstra.shortestPath(begin, end));
        }};
    }
}
