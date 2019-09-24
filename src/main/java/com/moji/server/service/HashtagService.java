package com.moji.server.service;

import com.moji.server.domain.Hashtag;
import com.moji.server.domain.HashtagCourse;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.HashtagReq;
import com.moji.server.repository.HashtagCourseRepository;
import com.moji.server.repository.HashtagRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;
    private final HashtagCourseRepository hashtagCourseRepository;

    // 생성자 의존성 주입
    public HashtagService(final HashtagRepository hashtagRepository,
                          final HashtagCourseRepository hashtagCourseRepository) {
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
    }

    // 해시태그 저장
    public DefaultRes saveHashtags(final HashtagReq hashtagReq){
        try{
            List<HashtagCourse> hashtagCourses = new ArrayList<>();
            for(int i = 0; i<hashtagReq.getHashtags().size(); i++){
                Hashtag hashtag = hashtagReq.getHashtags().get(i);

                Optional<Hashtag> selectedHashtag = hashtagRepository.findByTagInfo(hashtag.getTagInfo());
                String courseIdx = hashtagReq.getCourseIdx(); String tagIdx = hashtag.get_id();

                HashtagCourse hashtagCourse = new HashtagCourse();
                hashtagCourse.setTagIdx(tagIdx);
                hashtagCourse.setCourseIdx(courseIdx);
                if(selectedHashtag.isPresent()){ // 이미 등록된 해시태그일 경우
                    hashtagCourse.setTagIdx(selectedHashtag.get().get_id());
                    hashtagCourseRepository.save(hashtagCourse);
                }
                else{ // 새로 해시태그 등록하는 경우
                    String savedId = hashtagRepository.save(hashtag).get_id();
                    hashtagCourse.setTagIdx(savedId);
                    hashtagCourseRepository.save(hashtagCourse);
                }
            }
            return DefaultRes.res(StatusCode.CREATED, "해시태그가 등록 되었습니다.");
        }
        catch(Exception e){
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, "해시태그 등록에 실패하였습니다.");
        }
    }

    // 해시태그 조회
    public DefaultRes getHashtags(final String tag){
        Optional<List<Hashtag>> hashtag = hashtagRepository.findAllByTagInfoContaining(tag);
        return hashtag.map(value -> DefaultRes.res(StatusCode.OK, "사용자 정보 조회 완료", value)).orElseGet(() -> DefaultRes.res(StatusCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
