package com.moji.server.service;

import com.moji.server.domain.Comment;
import com.moji.server.domain.Course;
import com.moji.server.domain.Photo;
import com.moji.server.model.BoardReq;
import com.moji.server.model.CourseRes;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.CourseRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final LikeService likeService;
    private final CourseRepository courseRepository;
    private final S3FileUploadService s3MultipartService;

    // 생성자 의존성 주입
    public CourseService(final CourseRepository courseRepository,
                         final LikeService likeService,
                         final S3FileUploadService s3FileUploadService) 
    {
        this.courseRepository = courseRepository;
        this.likeService = likeService;
        this.s3MultipartService = s3FileUploadService;
    }

    //코스 저장
    @Transactional
    public DefaultRes saveCourse(final BoardReq board) {
        try {
            int size = board.getCourses().size();


            for (int i = 0; i < size; i++) {
                Course course = board.getCourses().get(i);
                course.setBoardIdx(board.getInfo().get_id());

                log.info(course.toString());

                //사진
                List<Photo> photos = new ArrayList<Photo>();
                for (int j = 0; j < course.getPhotos().size(); j++) {
                    Photo photo = course.getPhotos().get(j);
                    String url = s3MultipartService.upload(photo.getPhoto());

                    photo.setPhotoUrl(url);
                    photo.setPhoto(null);

                    photos.add(photo);
                }

                course.setPhotos(photos);

                courseRepository.save(course);
            }

            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * course 조회
     *
     * @param courseIdx
     * @return
     */
    public Course getCourse(String courseIdx) {
        return courseRepository.findBy_id(courseIdx);
    }

    /**
     * CommentService에서 받아온 comment 추가
     *
     * @param courseIdx
     * @param comment
     */
    public void saveComment(String courseIdx, Comment comment) {
        Course course = getCourse(courseIdx);

        List<Comment> commentList = course.getComments();
        commentList.add(comment);

        course.setComments(commentList);

        courseRepository.save(course);
    }

    public boolean isExistCourse(String postIdx) {
        return courseRepository.findBy_id(postIdx) != null;
    }

    /**
     * 코스별로 represent 속성을 true로 한 photo 가져오기
     * @param boardIdx
     * @return
     */
    public List<Course> getFirstRepresentPhotoByBoardIdx(String boardIdx) {
        return courseRepository.findByBoardIdxAndPhotosRepresentOrderByVisitTimeDesc(boardIdx, true);
    }

    /**
     * boardIdx에 맞는 course를 order 순서대로 가져오기
     * @param boardIdx
     * @return
     */
    public List<CourseRes> getCourseListByBoardIdx(String boardIdx, int userIdx) {
        List<Course> courseList = courseRepository.findByBoardIdxAndPhotosRepresentOrderByOrderAsc(boardIdx, true);

        log.info(courseList.toString());
        List<CourseRes> courseResList = new ArrayList<>();

        for (Course course: courseList) {
            CourseRes courseRes = new CourseRes();

            courseRes.setCourse(course);
            courseRes.setLikeCount(likeService.getCourseLikeCount(course.get_id()));
            courseRes.setLiked(likeService.isLikedCourse(course.get_id(), userIdx));
//            courseRes.setScrapCount(0); // TODO: scrap count
            courseRes.setScraped(true); // TODO: scrap 했는지
            courseResList.add(courseRes);
        }

        return courseResList;
    }
}
