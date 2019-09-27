package com.moji.server.repository;

import com.moji.server.domain.Course;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface CourseRepository extends MongoRepository<Course, String> {
    Course findBy_id(String idx);
    Course findBy_idAndVisitTimeBetween(String courseIdx, Date startDate, Date endDate);

//    @Query(value = "{'photos.represent':true, 'boardIdx': ?0}", fields="{'photos.photoUrl' : 1, '_id' : 0}")
//    @Query(value = "{'photos.represent':true, 'boardIdx': ?0}", sort = "{'visitTime': -1}")
    List<Course> findByBoardIdxAndPhotosRepresentOrderByVisitTimeDesc(String boardIdx, boolean represent);

    List<Course> findByBoardIdxAndPhotosRepresentOrderByOrderAsc(String boardIdx, boolean b);

    List<Course> findByUserIdx(final int userIdx);

    Optional<List<Course>> findAllByMainAddressContaining(final String keyword);
    Optional<List<Course>> findAllBySubAddressContaining(final String keyword);
}