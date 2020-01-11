package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.PowerPlant;
import org.springframework.data.repository.CrudRepository;


public interface PowerPlantRepo extends CrudRepository<PowerPlant, Long> {
    PowerPlant findByName(String name);
}
