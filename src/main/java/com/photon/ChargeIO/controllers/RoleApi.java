package com.photon.ChargeIO.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.photon.ChargeIO.mysql.entity.Role;
import com.photon.ChargeIO.mysql.entity.User;
import com.photon.ChargeIO.mysql.repository.RoleRepo;
import com.photon.ChargeIO.mysql.repository.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

@RestController
public class RoleApi {

    @Autowired
    private RoleRepo rRepo;

    @Autowired
    private UserRepo uRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating roles");
        LinkedList<String> rolesToAdd = new LinkedList<>(Arrays.asList("admin", "user"));
        for (String s: rolesToAdd) {
            try {
                Role r = new Role();
                r.setName(s);
                this.rRepo.save(r);
            } catch (DataIntegrityViolationException e) {
                System.out.println("[MYSQL] Role " + s + " already exists");
            }
        }
        System.out.println("[MYSQL] Roles genereted successfully");
    }

    @RequestMapping(value = "/user/role/", method = RequestMethod.GET)
    public HashMap<String, String> getUserRole(@RequestHeader("Auth") String auth) {
        try {
            String sub = JWT.require(Algorithm.HMAC256("secret")).build().verify(auth).getClaim("email").asString();
            User u = this.uRepo.findByEmail(sub);
            return new HashMap<String, String>(){{
                put("role", u.getRole().getName());
            }};
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is not valid8");
        }
    }
}
