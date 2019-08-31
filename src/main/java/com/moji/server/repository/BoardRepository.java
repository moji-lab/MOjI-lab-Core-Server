package com.moji.server.repository;

import com.moji.server.domain.Board;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface BoardRepository extends MongoRepository<Board, String> {
    Board findBy_id(String boardIdx);

    List<Board> findByOpen(boolean open);
}
