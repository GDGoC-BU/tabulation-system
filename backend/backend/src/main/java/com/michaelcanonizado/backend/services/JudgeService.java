package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.CLOSED
    })
    public JudgeSummaryDTO getJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(
                judge.getPageant()
                        .getId()
        );

        return judgeMapper.toSummaryDTO(judge);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING,
            PageantStatus.FINALIZING,
            PageantStatus.ONGOING
    })
    public List<JudgeSummaryDTO> getJudges() {
        UUID selectedPageantId = pageantContext.getId();
        return judgeRepository
                .findAllByPageant_Id(selectedPageantId)
                .stream()
                .map(judge -> {
                    return judgeMapper.toSummaryDTO(judge);
                })
                .toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public JudgeSummaryDTO updateJudge(UUID id, JudgeUpdateDTO judgeUpdateDTO) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update. Judge not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageantContext.assertAccess(
                judge.getPageant()
                        .getId()
        );

        judgeMapper.updateEntityFromDTO(judge, judgeUpdateDTO);
        return judgeMapper.toSummaryDTO(judgeRepository.save(judge));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public void deleteJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete. Judge not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        pageantContext.assertAccess(
                judge.getPageant()
                        .getId()
        );
        judgeRepository.delete(judge);
    }
}
