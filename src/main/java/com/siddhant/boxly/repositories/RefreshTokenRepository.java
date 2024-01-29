package com.siddhant.boxly.repositories;

import com.siddhant.boxly.entities.RefreshToken;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Integer> {
    Optional<RefreshToken> findByToken(String token);

    @Query(value = "select r from RefreshToken r where r.user.email=?1")
    Optional<RefreshToken> findByEmail(String email);
}
