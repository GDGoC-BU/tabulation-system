package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import com.michaelcanonizado.backend.utilities.CacheKeyBuilder;
import com.michaelcanonizado.backend.utilities.CacheNameConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JudgeService {
    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private JudgeMapper judgeMapper;

    @Autowired
    private PageantMapper pageantMapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private CacheKeyBuilder cacheKeyBuilder;

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public JudgeSummaryDTO getJudge(UUID id) {
        UUID selectedPageantId = pageantContext.getId();
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", selectedPageantId, "judges", id);

        JudgeSummaryDTO responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                JudgeSummaryDTO.class
        );

        if (responseDTO == null) {
            Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
                return new EntityNotFoundException(
                        "Judge not found!",
                        ErrorCode.ENTITY_NOT_FOUND
                );
            });
            pageantContext.assertAccess(judge.getPageant().getId());
            responseDTO = judgeMapper.toSummaryDTO(judge);

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.ONGOING
    })
    public List<JudgeSummaryDTO> getJudges() {
        UUID selectedPageantId = pageantContext.getId();
        String CACHE_NAME = CacheNameConstants.TABULATION;
        String CACHE_KEY = cacheKeyBuilder.build("pageants", selectedPageantId, "judges", "list", "all");

        List<JudgeSummaryDTO> responseDTO = cacheService.get(
                CACHE_NAME,
                CACHE_KEY,
                List.class
        );

        if (responseDTO == null) {
            responseDTO = judgeRepository
                    .findAllByPageant_Id(selectedPageantId)
                    .stream()
                    .map(judge -> {
                        return judgeMapper.toSummaryDTO(judge);
                    })
                    .toList();

            cacheService.put(
                    CACHE_NAME,
                    CACHE_KEY,
                    responseDTO
            );
        }

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public JudgeSummaryDTO updateJudge(UUID id, JudgeUpdateDTO judgeUpdateDTO) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Can't update. Judge not found!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });

        pageantContext.assertAccess(judge.getPageant().getId());
        judgeMapper.updateEntityFromDTO(judge, judgeUpdateDTO);
        Judge savedJudge = judgeRepository.save(judge);
        JudgeSummaryDTO responseDTO = judgeMapper.toSummaryDTO(savedJudge);

        UUID selectedPageantId = pageantContext.getId();
        cacheService.put(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "judges", id),
                responseDTO
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "judges", "list", "all")
        );

        return responseDTO;
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public void deleteJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Can't delete. Judge not found!",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        pageantContext.assertAccess(judge.getPageant().getId());

        UUID selectedPageantId = pageantContext.getId();
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "judges", id)
        );
        cacheService.evict(
                CacheNameConstants.TABULATION,
                cacheKeyBuilder.build("pageants", selectedPageantId, "judges", "list", "all")
        );

        judgeRepository.delete(judge);
    }
}
