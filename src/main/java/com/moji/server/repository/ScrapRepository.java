package com.moji.server.repository;

import com.moji.server.domain.Scrap;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Created By ds on 25/09/2019.
 */

public interface ScrapRepository extends JpaRepository<Scrap, Integer> {
    List<Scrap> findByUserIdx(final int userIdx);
    Optional<Scrap> findByUserIdxAndBoardIdx(final int userIdx, final String boardIdx);
    void deleteByBoardIdx(final String boardIdx);
}