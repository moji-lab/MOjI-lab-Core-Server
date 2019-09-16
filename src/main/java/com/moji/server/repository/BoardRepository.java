package com.moji.server.repository;

import com.moji.server.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

public interface BoardRepository extends MongoRepository<Board, String> {
    Board findBy_id(String boardIdx);
    Board findBy_idAndWriteTimeBetween(String boardIdx, LocalDateTime startDate, LocalDateTime endDate);

    List<Board> findByOpen(boolean open);
}
