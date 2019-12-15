package com.photon.ChargeIO.misc;

import com.photon.ChargeIO.mongo.document.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.util.Pair;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 DZIAŁA TYLKO JESLI POINTY SĄ POSEGREGOWANE PO NAME
 */
public class NodePreparator {
    List<Point> raw_list;
    DijkstraNode [] work_list;

    public NodePreparator(List<Point>list) throws Exception {
        this.raw_list = list;

        // sprawdzam czy lista jest posortowana;
        int max_name = -1;
        int last_name = 0;
        for(Point p : raw_list){
            int name = Integer.parseInt(p.getName());
            if(name > max_name) max_name = name;
            if(name < last_name) throw new Exception("DIJSTRA lista nie posortowana");
            last_name = name;
        }

        if(max_name+1 != raw_list.size()) throw new Exception("DIJKSTRA max_name+1:"+
                (1+max_name)+
                " != list_size:"+raw_list.size());
    }

    public DijkstraNode[] prepare() throws Exception {
        work_list = new DijkstraNode[raw_list.size()];

        for(Point point : raw_list)
            makeNode(point);

        return work_list;
    }

    private void makeNode(Point point) throws Exception {
        if(work_list == null) throw new Exception("DIJSTRA DUPA ni mo listy");
        DijkstraNode node = new DijkstraNode();

        int name = Integer.parseInt(point.getName());
        List<Integer> neighbours = point.getNeighbours();
        GeoJsonPoint pos = point.getPosition();

        for(Integer n_name : neighbours){
            Point n_point = findPoint(n_name);
            double len = pointsLength(pos, n_point.getPosition());
            node.addNeighbour(n_name,len);
        }

        if(work_list[name] != null) throw new Exception("DIJKSTRA powielenie name:"+name);
        work_list[name] = node;
    }

    private Point findPoint(int name) throws Exception {
        int min = 0, min_name = -1;
        int max = raw_list.size()-1, max_name = -1;
        int target = (min + max)/2, target_name = -1;

        Point max_point = null;
        Point min_point = null;
        Point target_point = null;


        while (min < max){
            min_point = raw_list.get(min);
            max_point = raw_list.get(max);
            target_point = raw_list.get(target);

            min_name = Integer.parseInt(min_point.getName());
            max_name = Integer.parseInt(max_point.getName());
            target_name = Integer.parseInt(target_point.getName());

            if(name == min_name) return  min_point;
            if(name == max_name) return  max_point;
            if(name == target_name) return target_point;

            if(name < target_name) max = target-1;
            else if (name > target_name) min = target+1;
            target = (min+max)/2;
        }

        throw new Exception("DIJKSTRA point name:"+name+" not found");
    }

    private double pointsLength(GeoJsonPoint pos1, GeoJsonPoint pos2){
        double dx = pos1.getX() - pos2.getX();
        double dy = pos1.getY() - pos2.getY();
        double lensq = dx*dx + dy*dy;
        return Math.sqrt(lensq);
    }
}
