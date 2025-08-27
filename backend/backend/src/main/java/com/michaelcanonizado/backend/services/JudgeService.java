package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.judge.JudgeCreateDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeDetailedDTO;
import com.michaelcanonizado.backend.dtos.judge.JudgeUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.JudgeMapper;
import com.michaelcanonizado.backend.models.Judge;
import com.michaelcanonizado.backend.repositories.JudgeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class JudgeService {
    @Autowired
    private JudgeRepository repository;

    @Autowired
    private JudgeMapper mapper;

    public JudgeDetailedDTO addJudge(JudgeCreateDTO judgeCreateDTO) {
        /* Add authentication! Password needs to be hashed:
           JudgeCreateDTO.password -> Judge.passwordHash */
        Judge judge = new Judge(judgeCreateDTO.username(), judgeCreateDTO.password());
        Judge savedJudge = repository.save(judge);
        return mapper.toDetailedDTO(judge);
    }

    public JudgeDetailedDTO getJudge(UUID id) {
        Judge judge = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });

        return mapper.toDetailedDTO(judge);
    }

    public List<JudgeDetailedDTO> getJudges() {
        return repository
                .findAll()
                .stream()
                .map(judge -> {
                    return mapper.toDetailedDTO(judge);
                })
                .toList();
    }

    public JudgeDetailedDTO updateJudge(UUID id, JudgeUpdateDTO judgeUpdateDTO) {
        Judge judge = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(judge, judgeUpdateDTO);
        return mapper.toDetailedDTO(repository.save(judge));
    }

    public void deleteJudge(UUID id) {
        Judge judge = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Judge not found!", ErrorCode.JUDGE_NOT_FOUND);
        });
        repository.delete(judge);
    }
}
