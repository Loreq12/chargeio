package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepo extends CrudRepository<User, Long> {
    User findByEmailAndPassword(String email, String password);
}
