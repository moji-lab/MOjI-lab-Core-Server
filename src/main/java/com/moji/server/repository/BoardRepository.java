package com.moji.server.repository;

import com.moji.server.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends MongoRepository<Board, String> {
    Optional<Board> findBy_id(String boardIdx);
    Optional<Board> findBy_idAndWriteTimeBetween(String boardIdx, LocalDate startDate, LocalDate endDate);
    List<Board> findByOpenOrderByWriteTimeDesc(boolean open);
    List<Board> findByUserIdx(final int userIdx);

    Optional<List<Board>> findAllByMainAddressContaining(final String keyword);
    Optional<List<Board>> findAllBySubAddressContaining(final String keyword);
}
