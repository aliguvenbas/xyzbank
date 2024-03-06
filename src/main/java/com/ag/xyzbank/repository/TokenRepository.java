package com.ag.xyzbank.repository;

import com.ag.xyzbank.repository.data.Token;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends JpaRepository<Token, Integer> {

	Token findByToken(String token);
}
