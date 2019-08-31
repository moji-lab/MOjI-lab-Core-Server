package com.moji.server.repository;

import com.moji.server.domain.LikeCourse;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface LikeCourseRepository extends MongoRepository<LikeCourse, String> {
    LikeCourse findByCourseIdxAndUserIdx(String postIdx, int userIdx);
}
