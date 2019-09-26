package com.moji.server.service;

import com.moji.server.model.DefaultRes;
import com.moji.server.model.ScrapReq;
import com.moji.server.repository.ScrapRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import static com.moji.server.model.DefaultRes.DB_ERROR;

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

    @Transactional
    public DefaultRes scrap(final ScrapReq scrapReq) {
        try {
            scrapRepository.save(scrapReq.toScrap());
            return DefaultRes.res(203, "Scrap 성공");
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DB_ERROR;
        }
    }
}