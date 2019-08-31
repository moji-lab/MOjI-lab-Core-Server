package com.moji.server.repository;

import com.moji.server.domain.LikeBoard;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeBoardRepository extends MongoRepository<LikeBoard, String> {
    LikeBoard findByBoardIdxAndUserIdx(String postIdx, int userIdx);

    int countByBoardIdx(String boardIdx);
}
