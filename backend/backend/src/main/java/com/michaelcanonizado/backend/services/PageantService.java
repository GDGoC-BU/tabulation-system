package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.entity.EntityAlreadyExistException;
import com.michaelcanonizado.backend.exceptions.entity.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PageantService {
    @Autowired
    private PageantRepository repository;

    @Autowired
    private PageantMapper mapper;

    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        if (repository.existsBy()) {
            throw new EntityAlreadyExistException("A pageant already exist! Only one is allowed.", ErrorCode.ENTITY_ALREADY_EXIST);
        }

        Pageant pageant = repository.save(mapper.toEntity(pageantCreateDTO));
        return mapper.toSummary(pageant);
    }

    public PageantSummaryDTO startPageant() {
        Pageant pageant = repository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.ONGOING);
        pageant.setStartedAt(LocalDateTime.now());

        return mapper.toSummary(repository.save(pageant));
    }

    public PageantSummaryDTO finalizePageant() {
        Pageant pageant = repository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.FINALIZING);

        return mapper.toSummary(repository.save(pageant));
    }

    public PageantSummaryDTO closePageant() {
        Pageant pageant = repository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.CLOSED);
        pageant.setEndedAt(LocalDateTime.now());

        return mapper.toSummary(repository.save(pageant));
    }

    public PageantSummaryDTO getPageant() {
        Pageant pageant = repository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toSummary(pageant);
    }

    public PageantSummaryDTO updatePageant(PageantUpdateDTO pageantUpdateDTO) {
        Pageant pageant = repository.findSingleton().orElseThrow(() -> {
            return new EntityNotFoundException("A pageant doesn't exist! Create a new one.", ErrorCode.ENTITY_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(pageant, pageantUpdateDTO);
        return mapper.toSummary(repository.save(pageant));
    }
}
