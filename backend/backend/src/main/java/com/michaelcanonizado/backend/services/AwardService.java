package com.michaelcanonizado.backend.services;

import com.github.javafaker.Faker;
import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.award.AwardCreateDTO;
import com.michaelcanonizado.backend.dtos.award.AwardSummaryDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.AwardMapper;
import com.michaelcanonizado.backend.models.*;
import com.michaelcanonizado.backend.repositories.AwardLeaderboardRepository;
import com.michaelcanonizado.backend.repositories.AwardRepository;
import com.michaelcanonizado.backend.repositories.CandidateRepository;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import com.michaelcanonizado.backend.contexts.PageantContext;
import com.michaelcanonizado.backend.utilities.AwardFormulaEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AwardService {
    @Autowired
    private AwardRepository awardRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private AwardLeaderboardRepository awardLeaderboardRepository;

    @Autowired
    private PageantRepository pageantRepository;

    @Autowired
    private AwardMapper mapper;

    @Autowired
    private PageantContext pageantContext;

    @Autowired
    private AwardFormulaEncoder formulaEncoder;

    @RequirePageantStatus({
            PageantStatus.PREPARATION,
    })
    public AwardSummaryDTO addAward(AwardCreateDTO awardCreateDTO) {
        /* Map DTO to entity */
        Award award = mapper.toEntity(awardCreateDTO);

        /* Connect to the selected pageant */
        UUID selectedPageantId = pageantContext.getId();
        Pageant pageant = pageantRepository.findById(selectedPageantId).orElseThrow(() -> {
            return new EntityNotFoundException(
                    "Cannot create candidate! Pageant being connected to it doesn't exist.",
                    ErrorCode.ENTITY_NOT_FOUND
            );
        });
        award.setPageant(pageant);

        /* Encode formula to SpEL safe format */
        String rawFormula = award.getFormula();
        String encodedFormula = formulaEncoder.encode(rawFormula);
        award.setFormula(encodedFormula);

        /* Save Award */
        Award savedAward = awardRepository.save(award);

        /* Get available candidates and pre-generate
           their rows in the award's leaderboard */
        List<Candidate> candidates = candidateRepository.findAllByPageant_Id(selectedPageantId);
        List<AwardLeaderboard> awardLeaderboards = new ArrayList<>();
        candidates.forEach(candidate -> {
            awardLeaderboards.add(
                    new AwardLeaderboard(
                            0,
                            candidate,
                            savedAward
                    )
            );
        });
        /* Batch save to minimize insert queries */
        awardLeaderboardRepository.saveAll(awardLeaderboards);

        /* Decode formula back to raw form since
           created award will be returned back. */
        encodedFormula = savedAward.getFormula();
        String decodedFormula = formulaEncoder.decode(encodedFormula);
        savedAward.setFormula(decodedFormula);
        return mapper.toSummaryDTO(savedAward);
    }

    public List<AwardSummaryDTO> getAwards() {
        UUID selectedPageantId = pageantContext.getId();

        List<Award> awards = awardRepository.findAllByPageant_Id(selectedPageantId);

        return awards
                .stream()
                .map(award -> {
                    String encodedFormula = award.getFormula();
                    String decodedFormula = formulaEncoder.decode(encodedFormula);
                    award.setFormula(decodedFormula);

                    return mapper.toSummaryDTO(award);
                })
                .toList();
    }
}
