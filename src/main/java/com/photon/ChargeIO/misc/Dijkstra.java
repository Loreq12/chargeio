package com.photon.ChargeIO.misc;


import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class Dijkstra {
    int size = -1;
    DijkstraNode [] nodes;
    double [] distances;
    boolean [] settled;
    int [] previous_node;

    public Dijkstra(DijkstraNode [] nodes){
        size = nodes.length;
        this.nodes = nodes;

        distances = new double[size];
        settled = new boolean[size];
        previous_node = new int [size];
    }

    private void clearData(){
        for(int i=0; i<size; ++i) distances[i] = Double.MAX_VALUE;
        for(int i=0; i<size; ++i) settled[i] = false;
        for(int i=0; i<size; ++i) previous_node[i] = -1;
    }

    public List<Integer> shortestPath(int begin, int end){
        clearData();
        List<Integer> path = new ArrayList<>();
        List<Integer> unsettled = new ArrayList<>();

        distances[end] = 0; // szukamy od end do bogin bo potem łatwiej trase wybrać
        unsettled.add(end);

        //szukanie scierzek
        while (!settled[begin] || unsettled.size()==0){
            int evaluated_index = min_unsettled(unsettled);

            for(Pair<Integer, Double> neighbour : nodes[evaluated_index]){
                int neighbour_index = neighbour.getFirst();
                double new_dist = distances[evaluated_index] + neighbour.getSecond();

                if(settled[neighbour_index]) continue;

                if(new_dist < distances[neighbour_index]){
                    previous_node[neighbour_index] = evaluated_index;
                    distances[neighbour_index] = new_dist;
                }

                if(!unsettled.contains(neighbour_index)) unsettled.add(neighbour_index);
            }

            unsettled.remove(new Integer(evaluated_index));
            settled[evaluated_index] = true;
        }

        // tworzenie trasy
        int current_index = begin;
        while( current_index != end ){
            path.add(current_index);
            current_index = previous_node[current_index];
        }
        path.add(current_index);

        return path;
    }

    private int min_unsettled(List<Integer> uns){
        int min_index = -1;
        double min_value = Double.MAX_VALUE;

        for(int index : uns)
            if(min_value > distances[index]){
                min_index = index;
                min_value = distances[index];
            }

        return min_index;
    }
}
