package com.photon.ChargeIO.controllers;

import com.photon.ChargeIO.mysql.entity.Role;
import com.photon.ChargeIO.mysql.entity.User;
import com.photon.ChargeIO.mysql.repository.RoleRepo;
import com.photon.ChargeIO.mysql.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi {

    @Autowired
    private UserRepo uRepo;

    @Autowired
    private RoleRepo rRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating  master user");

        Role adminRole = this.rRepo.findByName("kurwa");

        User admin = new User();
        admin.setEmail("admin@charge.io");
        admin.setPassword("zaq1@WSX");
        admin.setRole(adminRole);

        try {
            this.uRepo.save(admin);
        } catch (DataIntegrityViolationException e) {
            System.out.println("[MYSQL] Master user already exist");
        }
        System.out.println("[MYSQL] Users genereted successfully");
    }

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public String loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        User u = this.uRepo.findByEmailAndPassword(email, password);
        if (u == null)
            return "Ni ma takiego usera";
        return u.toString();
    }


}
