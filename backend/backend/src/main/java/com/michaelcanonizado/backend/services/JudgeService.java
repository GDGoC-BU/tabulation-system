package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class JudgeService {
    @Autowired
    private JudgeRepository repository;

    @Autowired
    private JudgeMapper mapper;

    public JudgeSummaryDTO addJudge(JudgeCreateDTO judgeCreateDTO) {
        Judge judge = repository.save(mapper.toEntity(judgeCreateDTO));
        return mapper.toSummaryDTO(judge);
    }

    public JudgeSummaryDTO getJudge(UUID id) {
        Judge judge = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });

        return mapper.toSummaryDTO(judge);
    }
}
