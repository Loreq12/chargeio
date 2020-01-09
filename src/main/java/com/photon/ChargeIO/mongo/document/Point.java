package com.photon.ChargeIO.mongo.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Point {

    @Id
    private String id;
    private String name;
    private List<Integer> neighbours;
    private org.springframework.data.geo.Point position;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getNeighbours() {
        return neighbours;
    }

    public void setNeighbours(List<Integer> neighbours) {
        this.neighbours = neighbours;
    }

    public org.springframework.data.geo.Point getPosition() {
        return position;
    }

    public void setPosition(org.springframework.data.geo.Point position) {
        this.position = position;
    }
}
