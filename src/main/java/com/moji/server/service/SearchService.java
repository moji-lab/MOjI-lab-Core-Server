package com.moji.server.service;

import com.moji.server.domain.Hashtag;
import com.moji.server.model.DefaultRes;
import com.moji.server.repository.HashtagCourseRepository;
import com.moji.server.repository.HashtagRepository;
import com.moji.server.repository.SearchCourseRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class SearchService {
    private final HashtagRepository hashtagRepository;
    private final HashtagCourseRepository hashtagCourseRepository;
    private final SearchCourseRepository searchCourseRepository;

    // 생성자 의존성 주입
    public SearchService(final HashtagRepository hashtagRepository,
                         final HashtagCourseRepository hashtagCourseRepository,
                         final SearchCourseRepository searchCourseRepository) {
        this.hashtagRepository = hashtagRepository;
        this.hashtagCourseRepository = hashtagCourseRepository;
        this.searchCourseRepository = searchCourseRepository;
    }

    public DefaultRes getSearchResultByHashtag(final String keyword){
        Optional<List<Hashtag>> hashtag = hashtagRepository.findByTagInfoContaining(keyword);

        return hashtag.map(value ->
                DefaultRes.res(StatusCode.OK, "검색 성공",
                        searchCourseRepository.findBy_id(
                                hashtagCourseRepository.findBytagIdx(value.iterator().next().get_id())
                                        .get().getCourseIdx())))
                .orElseGet(() -> DefaultRes.res(StatusCode.NOT_FOUND, "검색 실패"));
    }
}