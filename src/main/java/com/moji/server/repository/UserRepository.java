package com.moji.server.repository;

import com.moji.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import java.util.*;

/**
 * Created By ds on 2019-08-20.
 */

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmailAndPassword(final String email, final String password);
    User findByUserIdx(int userIdx);
    Optional<User> findByNickname(String name);
    Optional<User> findByEmail(String email);
}