package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.AwardMapper;
import com.michaelcanonizado.backend.models.Award;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.AwardRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.contexts.PageantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AwardService {
    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private AwardMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public AwardSummaryDTO addAward(AwardCreateDTO awardCreateDTO) {
        Award award = mapper.toEntity(awardCreateDTO);

        UUID currentPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(currentPageantId).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Cannot create candidate! Pageant being connected to it doesn't exist.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        award.setPageant(pageant);

        Award savedAward = awardRepository.save(award);
        return mapper.toSummaryDTO(savedAward);
    }
}
