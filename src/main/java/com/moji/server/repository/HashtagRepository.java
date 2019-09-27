package com.moji.server.repository;

import com.moji.server.domain.Hashtag;
import com.moji.server.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface HashtagRepository extends MongoRepository<Hashtag, String> {
    Optional <List<Hashtag>> findAllByTagInfoContaining(final String tag);
    Optional<Hashtag> findByTagInfo(final String tagInfo);
}