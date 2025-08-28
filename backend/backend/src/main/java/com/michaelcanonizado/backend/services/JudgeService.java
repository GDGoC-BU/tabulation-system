package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeDetailedDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.models.Candidate;
import com.michaelcanonizado.backend.models.Criterion;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.models.Score;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.CriterionRepository;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import com.michaelcanonizado.backend.repositories.ScoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JudgeService {
    @Autowired
    private JudgeRepository judgeRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private CriterionRepository criterionRepository;

    @Autowired
    private ScoreRepository scoreRepository;

    @Autowired
    private JudgeMapper mapper;

    public JudgeDetailedDTO addJudge(JudgeCreateDTO judgeCreateDTO) {
        /* Add authentication! Password needs to be hashed:
           JudgeCreateDTO.password -> Judge.passwordHash */
        Judge judge = new Judge(judgeCreateDTO.username(), judgeCreateDTO.password());
        Judge savedJudge = judgeRepository.save(judge);

        /* Pre-generate the scores for the new judge */
        List<Candidate> candidates = candidateRepository.findAll();
        List<Criterion> criteria = criterionRepository.findAll();
        candidates.forEach(candidate -> {
            criteria.forEach(criterion -> {
                scoreRepository.save(new Score(0, savedJudge, candidate, criterion));
            });
        });

        return mapper.toDetailedDTO(savedJudge);
    }

    public JudgeDetailedDTO getJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });

        return mapper.toDetailedDTO(judge);
    }

    public List<JudgeDetailedDTO> getJudges() {
        return judgeRepository
                .findAll()
                .stream()
                .map(judge -> {
                    return mapper.toDetailedDTO(judge);
                })
                .toList();
    }

    public JudgeDetailedDTO updateJudge(UUID id, JudgeUpdateDTO judgeUpdateDTO) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(judge, judgeUpdateDTO);
        return mapper.toDetailedDTO(judgeRepository.save(judge));
    }

    public void deleteJudge(UUID id) {
        Judge judge = judgeRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });
        judgeRepository.delete(judge);
    }
}
