package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PageantService {
    @Autowired
    private PageantRepository repository;

    @Autowired
    private PageantMapper mapper;

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        Pageant pageant = repository.save(mapper.toEntity(pageantCreateDTO));
        return mapper.toSummary(pageant);
    }

    public PageantSummaryDTO startPageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.ONGOING);
        pageant.setStartedAt(LocalDateTime.now());

        return mapper.toSummary(repository.save(pageant));
    }

    public PageantSummaryDTO finalizePageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.FINALIZING);

        return mapper.toSummary(repository.save(pageant));
    }

    public PageantSummaryDTO closePageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.CLOSED);
        pageant.setEndedAt(LocalDateTime.now());

        return mapper.toSummary(repository.save(pageant));
    }

    public PageantSummaryDTO getPageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toSummary(pageant);
    }

    public List<PageantSummaryDTO> getPageants() {
        List<Pageant> pageants = repository.findAll();
        return pageants
                .stream()
                .map(pageant -> {
                    return mapper.toSummary(pageant);
                }).toList();
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public PageantSummaryDTO updatePageant(UUID id, PageantUpdateDTO pageantUpdateDTO) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        mapper.updateEntityFromDTO(pageant, pageantUpdateDTO);
        return mapper.toSummary(repository.save(pageant));
    }

    public void deletePageant(UUID id) {
        if (!repository.existsById(id)) {
            throw new EntityNotFoundException("Can't delete! Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        }

        repository.deleteById(id);
    }
}
