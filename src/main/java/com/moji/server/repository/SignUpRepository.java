package com.moji.server.repository;

import com.moji.server.domain.SignUp;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Created By ds on 26/09/2019.
 */

public interface SignUpRepository extends JpaRepository<SignUp, Integer> {
    Optional<SignUp> findByEmailAndPassword(final String email, final String password);
}