package com.photon.ChargeIO.controllers;

import com.photon.ChargeIO.mongo.document.Point;
import com.photon.ChargeIO.mysql.entity.Role;
import com.photon.ChargeIO.mysql.repository.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@RestController
public class RoleApi {

    @Autowired
    private RoleRepo repo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating roles");
        LinkedList<String> rolesToAdd = new LinkedList<>(Arrays.asList("admin", "user"));
        for (String s: rolesToAdd) {
            try {
                Role r = new Role();
                r.setName(s);
                this.repo.save(r);
            } catch (DataIntegrityViolationException e) {
                System.out.println("[MYSQL] Role " + s + " already exists");
            }
        }
        System.out.println("[MYSQL] Roles genereted successfully");
    }

    @RequestMapping(value = "/roles/", method = RequestMethod.GET)
    public List<Role> listAllPoints() {
        List<Role> l = new LinkedList<>();
        this.repo.findAll().forEach(l::add);
        return l;
    }
}
