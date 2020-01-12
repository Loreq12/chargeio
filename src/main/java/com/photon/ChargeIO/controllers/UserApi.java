package com.photon.ChargeIO.controllers;

import com.auth0.jwt.algorithms.Algorithm;
import com.photon.ChargeIO.mysql.entity.Role;
import com.photon.ChargeIO.mysql.entity.TransactionHistory;
import com.photon.ChargeIO.mysql.entity.User;
import com.photon.ChargeIO.mysql.entity.Wallet;
import com.photon.ChargeIO.mysql.repository.RoleRepo;
import com.photon.ChargeIO.mysql.repository.TransactionRepo;
import com.photon.ChargeIO.mysql.repository.UserRepo;
import com.photon.ChargeIO.mysql.repository.WalletRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import com.auth0.jwt.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import java.sql.Date;

@RestController
public class UserApi {

    @Autowired
    private UserRepo uRepo;

    @Autowired
    private RoleRepo rRepo;

    @Autowired
    private WalletRepo wRepo;

    @Autowired
    private TransactionRepo tRepo;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        System.out.println("[MYSQL] Creating users");

        HashMap<String, List<String>> data = new HashMap<>(){{
            put("user", Arrays.asList("admin@charge.io", "user@gmail.com"));
            put("password", Arrays.asList("zaq1@WSX", "zaq1@WSX"));
            put("role", Arrays.asList("admin", "user"));
        }};

        for (int i = 0; i < data.get("user").size(); i++) {
            User admin = new User();
            admin.setEmail(data.get("user").get(i));
            admin.setPassword(BCrypt.hashpw(data.get("password").get(i), BCrypt.gensalt()));
            admin.setRole(this.rRepo.findByName(data.get("role").get(i)));

            Wallet newwallet = new Wallet();
            newwallet.setAccountNumber(UUID.randomUUID().toString());
            newwallet.setAmount(0.0);
            wRepo.save(newwallet);

            admin.setWallet(newwallet);

            try {
                this.uRepo.save(admin);
            } catch (DataIntegrityViolationException e) {
                System.out.println("[MYSQL] User already exist");
            }


        }
        System.out.println("[MYSQL] Users genereted successfully");
    }

    @RequestMapping(value = "/register/", method = RequestMethod.POST)
    public HashMap<String, String> registerUser(
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("firstName") String firstName,
            @RequestParam("lastName") String lastName,
            @RequestParam("birthdate") String birthdate) {

        Role r = rRepo.findByName("user");

        Wallet w = new Wallet();
        w.setAccountNumber(UUID.randomUUID().toString());
        w.setAmount(0.0);
        wRepo.save(w);

        User u = new User();
        u.setRole(r);
        u.setWallet(w);
        u.setEmail(email);
        u.setFirstName(firstName);
        u.setLastName(lastName);
        try {
            u.setBirtdate(new SimpleDateFormat("dd/MM/yyyy").parse(birthdate));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        u.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        uRepo.save(u);

        return new HashMap<String, String>(){{
            put("access_token", JWT.create().withClaim("email", u.getEmail()).sign(Algorithm.HMAC256("secret")));}};
    }

    @RequestMapping(value = "/login/", method = RequestMethod.POST)
    public HashMap<String, String> loginUser(@RequestParam("email") String email, @RequestParam("password") String password) {
        User u = this.uRepo.findByEmail(email);
        if (u == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        if (!BCrypt.checkpw(password, u.getPassword()))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Incorrect password");
        return new HashMap<String, String>(){{
            put("access_token", JWT.create().withClaim("email", u.getEmail()).sign(Algorithm.HMAC256("secret")));}};
    }


    @RequestMapping(value = "/addmoney/", method = RequestMethod.POST)
    public String addmoney(@RequestParam("email") String email, @RequestParam("money") double money) {
        User u = this.uRepo.findByEmail(email);
        if (u == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        Wallet wallet = u.getWallet();
        double saldo = wallet.getAmount();
        wallet.setAmount(saldo + money);

        wRepo.save(wallet);

        TransactionHistory history = new TransactionHistory();
        history.setDate(new Date(System.currentTimeMillis()));
        history.setWallet(wallet);
        history.setAmmont_change(money);

        tRepo.save(history);

        return "success";
    }

    @RequestMapping(value = "/getmoney/", method = RequestMethod.POST)
    public double  getmoney(@RequestParam("email") String email) {
        User u = this.uRepo.findByEmail(email);
        if (u == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        Wallet wallet = u.getWallet();
        double saldo = wallet.getAmount();



        return saldo;
    }


    @RequestMapping(value = "/getransactions/", method = RequestMethod.POST)
    public List<TransactionHistory>  gettransactions(@RequestParam("email") String email) {
        User u = this.uRepo.findByEmail(email);
        if (u == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        Wallet wallet = u.getWallet();
        return tRepo.findByWallet(wallet);
    }

    @RequestMapping(value = "/payment/", method = RequestMethod.POST)
    public String payment(@RequestParam("email") String email, @RequestParam("power") double power) {
        User u = this.uRepo.findByEmail(email);
        if (u == null)
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

        Wallet w = u.getWallet();
        double am = w.getAmount();
        w.setAmount(am - power);

        TransactionHistory hist = new TransactionHistory();
        hist.setWallet(w);
        hist.setAmmont_change(-power);
        tRepo.save(hist);

        return "payedXD";
    }
}
