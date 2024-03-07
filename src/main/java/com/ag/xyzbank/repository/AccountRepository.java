package com.ag.xyzbank.repository;

import com.ag.xyzbank.repository.data.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountRepository extends JpaRepository<Account, Integer> {
	Account findByUsername(String username);

	Account findByIban(String iban);
}
