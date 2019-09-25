package com.moji.server.repository;

import com.moji.server.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By ds on 25/09/2019.
 */

public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
}