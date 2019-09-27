package com.moji.server.repository;

import com.moji.server.domain.Course;
import com.moji.server.domain.Hashtag;
import com.moji.server.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface SearchCourseRepository extends MongoRepository<Course, String> {
    Optional<Course> findBy_id(final String id);
}
