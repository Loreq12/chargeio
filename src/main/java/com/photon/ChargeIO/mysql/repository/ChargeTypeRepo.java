package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.ChargeType;
import org.springframework.data.repository.CrudRepository;

public interface ChargeTypeRepo extends CrudRepository<ChargeType, Long> {
    
}
