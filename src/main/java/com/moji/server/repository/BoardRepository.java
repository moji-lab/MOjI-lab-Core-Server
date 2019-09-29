package com.moji.server.repository;

import com.moji.server.domain.Board;
import com.moji.server.domain.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends MongoRepository<Board, String> {
    Board findBy_id(String boardIdx);
    Board findBy_idAndWriteTimeBetween(String boardIdx, LocalDate startDate, LocalDate endDate);
    List<Board> findByOpenOrderByWriteTimeDesc(boolean open);
    List<Board> findByUserIdx(final int userIdx);

    Optional<List<Board>> findAllByMainAddressContaining(final String keyword);
    Optional<List<Board>> findAllBySubAddressContaining(final String keyword);
}
