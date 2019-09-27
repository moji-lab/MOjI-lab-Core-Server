package com.moji.server.repository;

import com.moji.server.domain.Hashtag;
import com.moji.server.domain.HashtagCourse;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HashtagCourseRepository extends MongoRepository<HashtagCourse, String> {
    Optional<List<HashtagCourse>> findAllBytagIdx(final String tagIdx);
}