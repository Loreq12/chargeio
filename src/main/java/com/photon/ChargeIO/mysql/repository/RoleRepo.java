package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {
    Role findByName(String name);
}
