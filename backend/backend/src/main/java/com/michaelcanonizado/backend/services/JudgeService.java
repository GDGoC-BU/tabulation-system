package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JudgeService {
    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private JudgeMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    public JudgeSummaryDTO getJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toSummaryDTO(judge);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
            PageantStatus.ONGOING
    })
    public List<JudgeSummaryDTO> getJudges() {
        return judgeRepository
                .findAll()
                .stream()
                .map(judge -> {
                    return mapper.toSummaryDTO(judge);
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

        mapper.updateEntityFromDTO(judge, judgeUpdateDTO);
        return mapper.toSummaryDTO(judgeRepository.save(judge));
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public void deleteJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete. Judge not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
        judgeRepository.delete(judge);
    }
}
