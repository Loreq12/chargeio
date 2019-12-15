package com.photon.ChargeIO.misc;


import org.springframework.data.util.Pair;
import java.util.LinkedList;

/**
 * LISTA SOMSIADÓW
 * First - id somsiada
 * Second - odpleglosc do somsiada
 */
public class DijkstraNode extends LinkedList<Pair<Integer, Double>> {

    public void addNeighbour(int name, double dist){
        Pair<Integer,Double> p = Pair.of(name, dist);
        add(p);
    }

    public int getName(int index){
        return get(index).getFirst();
    }

    public double getDistance(int index){
        return get(index).getSecond();
    }
}
