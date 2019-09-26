package com.moji.server.service;

import com.moji.server.domain.Scrap;
import com.moji.server.model.DefaultRes;
import com.moji.server.model.ScrapReq;
import com.moji.server.repository.ScrapRepository;
import com.moji.server.util.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.Optional;

import static com.moji.server.model.DefaultRes.DB_ERROR;
import static com.moji.server.model.DefaultRes.NOT_FOUNT;

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

    @Transactional
    public DefaultRes deleteScrap(final ScrapReq scrapReq) {
        try {
            Optional<Scrap> scrap = scrapRepository.findByUserIdxAndBoardIdx(scrapReq.getUserIdx(), scrapReq.getBoardIdx());
            if (scrap.isPresent()) {
                scrapRepository.deleteById(scrap.get().getScrapIdx());
                return DefaultRes.res(StatusCode.NO_CONTENT, "스크랩 삭제 성공");
            }
            return NOT_FOUNT;
        } catch (Exception e) {
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DB_ERROR;
        }
    }
}