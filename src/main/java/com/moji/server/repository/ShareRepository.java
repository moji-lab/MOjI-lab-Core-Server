package com.moji.server.repository;

import com.moji.server.domain.Share;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created By ds on 25/09/2019.
 */

public interface ShareRepository extends JpaRepository<Share, Integer> {
}