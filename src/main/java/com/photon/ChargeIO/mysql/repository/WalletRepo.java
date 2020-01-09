package com.photon.ChargeIO.mysql.repository;

import com.photon.ChargeIO.mysql.entity.Wallet;
import org.springframework.data.repository.CrudRepository;

public interface WalletRepo extends CrudRepository<Wallet, Long> {
}
