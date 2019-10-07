package com.moji.server.service;

import com.moji.server.domain.Comment;
import com.moji.server.domain.Course;
import com.moji.server.domain.Photo;
import com.moji.server.model.BoardReq;
import com.moji.server.model.BoardRes2;
import com.moji.server.model.CourseRes;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.CourseRepository;
import com.moji.server.util.ResponseMessage;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class CourseService {

    BoardRes2 data = BoardRes2.getBoardRes2();
    private final LikeService likeService;
    private final CourseRepository courseRepository;
    private final S3FileUploadService s3MultipartService;

    // 생성자 의존성 주입
    public CourseService(final CourseRepository courseRepository,
                         final LikeService likeService,
                         final S3FileUploadService s3FileUploadService) {
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
                Course course = (Course) board.getCourses().get(i).clone();
                course.setBoardIdx(board.getInfo().get_id());
                course.setUserIdx(board.getInfo().getUserIdx());

                //사진
                List<Photo> photos = new ArrayList<Photo>();
                for (int j = 0; j < course.getPhotos().size(); j++) {

                    Photo photo = (Photo) course.getPhotos().get(j).clone();
                    String url = s3MultipartService.upload(photo.getPhoto());

                    photo.setPhotoUrl(url);
                    photo.setPhoto(null);

                    photos.add(photo);

                    board.getCourses().get(i).getPhotos().get(j).setPhoto(null);
                    board.getCourses().get(i).getPhotos().get(j).setPhotoUrl(url);
                }

                course.setPhotos(photos);

                courseRepository.save(course);
                data.getCourseIdx().add(course.get_id());
            }


            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATE_BOARD);
        } catch (Exception e) {
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //코스 공ㅇㅍ
    @Transactional
    public DefaultRes shareCourse(final BoardReq board) {
        try {
            int size = board.getCourses().size();

            for (int i = 0; i < size; i++) {
                Course course = (Course) board.getCourses().get(i).clone();

                course.setBoardIdx(board.getInfo().get_id());
                course.setUserIdx(board.getInfo().getUserIdx());

                courseRepository.save(course);
                data.getCourseIdx().add(course.get_id());
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
     *
     * @param boardIdx
     * @return
     */
    public List<Course> getFirstRepresentPhotoByBoardIdx(String boardIdx) {
        return courseRepository.findByBoardIdxAndPhotosRepresentOrderByVisitTimeDesc(boardIdx, true);
    }

    /**
     * boardIdx에 맞는 course를 order 순서대로 가져오기
     *
     * @param boardIdx
     * @return
     */
    public List<CourseRes> getCourseListByBoardIdx(String boardIdx, int userIdx) {
        List<Course> courseList = courseRepository.findByBoardIdxAndPhotosRepresentOrderByOrderAsc(boardIdx, true);
        List<CourseRes> courseResList = new ArrayList<>();

        for (Course course : courseList) {
            CourseRes courseRes = new CourseRes();

            courseRes.setCourse(course);
            courseRes.setLikeCount(likeService.getCourseLikeCount(course.get_id()));
            courseRes.setLiked(likeService.isLikedCourse(course.get_id(), userIdx));
            courseRes.setScraped(true); // TODO: scrap 했는지
            courseResList.add(courseRes);
        }
        return courseResList;
    }

    @Transactional
    public void deleteAllCourse(final String boardIdx) {
        try {
            List<Course> courseList = courseRepository.findByBoardIdx(boardIdx);
            for (Course c : courseList) {
                likeService.deleteCourseLike(c.get_id());
            }
            courseRepository.deleteByBoardIdx(boardIdx);
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        }
    }

    @Transactional
    public DefaultRes isOpenPhoto(final String courseIdx, final int photoIdx, final int userIdx) {
        try {
            Course course = courseRepository.findBy_id(courseIdx);
            if (course.getUserIdx() != userIdx) return DefaultRes.UNAUTHORIZED;

            if (course.getPhotos().get(photoIdx).isRepresent())
                course.getPhotos().get(photoIdx).setRepresent(false);
            else course.getPhotos().get(photoIdx).setRepresent(true);

            courseRepository.save(course);

            return DefaultRes.res(204, "사진 공개 변경 성공", course.getPhotos().get(photoIdx).isRepresent());
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.DB_ERROR;
        }
    }
}