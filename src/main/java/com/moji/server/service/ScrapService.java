package com.moji.server.service;

import com.moji.server.model.DefaultRes;
import com.moji.server.repository.ScrapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * Created By ds on 25/09/2019.
 */

@Slf4j
@Service
public class ScrapService {

    private final ScrapRepository scrapRepository;

    public ScrapService(final ScrapRepository scrapRepository) {
        this.scrapRepository = scrapRepository;
    }

    public DefaultRes scrap() {
        return null;
    }
}