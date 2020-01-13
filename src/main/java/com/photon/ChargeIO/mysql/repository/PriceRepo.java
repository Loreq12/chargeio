package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.Price;
import org.springframework.data.repository.CrudRepository;

public interface PriceRepo extends CrudRepository<Price, Long> {
    
}
