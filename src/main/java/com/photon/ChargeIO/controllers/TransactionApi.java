package com.photon.ChargeIO.controllers;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.photon.ChargeIO.mysql.entity.TransactionHistory;
import com.photon.ChargeIO.mysql.entity.User;
import com.photon.ChargeIO.mysql.entity.Wallet;
import com.photon.ChargeIO.mysql.repository.TransactionRepo;
import com.photon.ChargeIO.mysql.repository.UserRepo;
import com.photon.ChargeIO.mysql.repository.WalletRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController

public class TransactionApi {

    @Autowired
    private UserRepo uRepo;

    @Autowired
    private WalletRepo wRepo;

    @Autowired
    private TransactionRepo tRepo;

    @RequestMapping(value = "/money/", method = RequestMethod.POST)
    public String addmoney(@RequestHeader("Auth") String auth, @RequestParam("money") double money) {
        try {
            String email = JWT.require(Algorithm.HMAC256("secret")).build().verify(auth).getClaim("email").asString();
            
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
            throw new ResponseStatusException(HttpStatus.OK, "");
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is not valid");
        }
    }

    @RequestMapping(value = "/money/", method = RequestMethod.GET)
    public HashMap<String, Double> getmoney(@RequestHeader("Auth") String auth) {
        try {
            String email = JWT.require(Algorithm.HMAC256("secret")).build().verify(auth).getClaim("email").asString();
            User u = this.uRepo.findByEmail(email);
            if (u == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");

            Wallet wallet = u.getWallet();
            double saldo = wallet.getAmount();
            return new HashMap<String, Double>(){{
                put("amount", saldo);
            }};
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is not valid");
        }
    }

    @RequestMapping(value = "/transactions/", method = RequestMethod.GET)
    public HashMap<String, List<TransactionHistory>> gettransactions(@RequestHeader("Auth") String auth) {
        try{
            String email = JWT.require(Algorithm.HMAC256("secret")).build().verify(auth).getClaim("email").asString();
            User u = this.uRepo.findByEmail(email);
            if (u == null)
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
    
            Wallet wallet = u.getWallet();
            return new HashMap<String, List<TransactionHistory>>(){{
                put("transactions", tRepo.findByWallet(wallet));
            }};
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is not valid");
        }
    }

    @RequestMapping(value = "/payment/", method = RequestMethod.POST)
    public String payment(@RequestHeader("Auth") String auth, @RequestParam("power") double power) {
        try {
            String email = JWT.require(Algorithm.HMAC256("secret")).build().verify(auth).getClaim("email").asString();
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
            throw new ResponseStatusException(HttpStatus.OK, "");
        } catch (JWTVerificationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authorization token is not valid");
        }
    }
    
}