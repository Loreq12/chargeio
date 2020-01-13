package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.TransactionHistory;
import com.photon.ChargeIO.mysql.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TransactionRepo extends CrudRepository<TransactionHistory, Long> {
    List<TransactionHistory> findByWallet(Wallet w);
}
