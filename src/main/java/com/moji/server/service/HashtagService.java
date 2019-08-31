package com.moji.server.service;

import com.moji.server.domain.Hashtag;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.HashtagReq;
import com.moji.server.repository.HashtagRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class HashtagService {
    private final HashtagRepository hashtagRepository;

    // 생성자 의존성 주입
    public HashtagService(final HashtagRepository hashtagRepository) {
        this.hashtagRepository = hashtagRepository;
    }

    public DefaultRes saveHashtags(final HashtagReq hashtagReq){
        try{
            for(int i = 0; i<hashtagReq.getHashtags().size(); i++){
                hashtagRepository.save(hashtagReq.getHashtags().get(i));
            }
            return DefaultRes.res(StatusCode.CREATED, "해시태그가 등록 되었습니다.");
        }
        catch(Exception e){
            log.info(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, "해시태그 등록에 실패하였습니다.");
        }
    }

    public DefaultRes getHashtags(final String tag){
        Optional<List<Hashtag>> hashtag = hashtagRepository.findByTagInfoContaining(tag);
        return hashtag.map(value -> DefaultRes.res(StatusCode.OK, "사용자 정보 조회 완료", value)).orElseGet(() -> DefaultRes.res(StatusCode.NOT_FOUND, "사용자를 찾을 수 없습니다."));
    }
}
