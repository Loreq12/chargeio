package com.photon.ChargeIO.mongo.repository;

import com.photon.ChargeIO.mongo.document.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepo extends MongoRepository<Point, String> {
    Point findTop1ByPositionNear(org.springframework.data.geo.Point p);
}
