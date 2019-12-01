package com.photon.ChargeIO.mongo.repository;

import com.photon.ChargeIO.mongo.document.Point;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointRepo extends MongoRepository<Point, String> { }
