package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.annotations.RequirePageantStatus;
import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantHierarchyDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.exceptions.customs.PageantAccessDeniedException;
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

    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        Pageant pageant = repository.save(mapper.toEntity(pageantCreateDTO));
        return mapper.toSummaryDTO(pageant);
    }

    @RequirePageantStatus({
            PageantStatus.PREPARATION
    })
    public PageantSummaryDTO startPageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.ONGOING);
        pageant.setStartedAt(LocalDateTime.now());

        return mapper.toSummaryDTO(repository.save(pageant));
    }

    @RequirePageantStatus({
            PageantStatus.ONGOING
    })
    public PageantSummaryDTO finalizePageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.FINALIZING);

        return mapper.toSummaryDTO(repository.save(pageant));
    }

    @RequirePageantStatus({
            PageantStatus.FINALIZING
    })
    public PageantSummaryDTO closePageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        pageant.setStatus(PageantStatus.CLOSED);
        pageant.setEndedAt(LocalDateTime.now());

        return mapper.toSummaryDTO(repository.save(pageant));
    }

    public PageantSummaryDTO getPageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toSummaryDTO(pageant);
    }
    public PageantHierarchyDTO getPageantHierarchy(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Pageant not found!", ErrorCode.ENTITY_NOT_FOUND);
        });

        return mapper.toHierarchyDTO(pageant);
    }

    public List<PageantSummaryDTO> getPageants() {
        List<Pageant> pageants = repository.findAll();
        return pageants
                .stream()
                .map(pageant -> {
                    return mapper.toSummaryDTO(pageant);
                }).toList();
    }

    public PageantSummaryDTO updatePageant(UUID id, PageantUpdateDTO pageantUpdateDTO) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't update! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Manually do the status check. Don't use @RequirePageantStatus() */
        if (
            pageant.getStatus() != PageantStatus.PREPARATION &&
            pageant.getStatus() != PageantStatus.CLOSED
        ) {
            throw new PageantAccessDeniedException(
                    "Can't update! A pageant can only be updated before or after starting.",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }

        mapper.updateEntityFromDTO(pageant, pageantUpdateDTO);
        return mapper.toSummaryDTO(repository.save(pageant));
    }

    public void deletePageant(UUID id) {
        Pageant pageant = repository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Can't delete! Pageant not found.", ErrorCode.ENTITY_NOT_FOUND);
        });

        /* Manually do the status check. Don't use @RequirePageantStatus() */
        if (
            pageant.getStatus() != PageantStatus.PREPARATION &&
            pageant.getStatus() != PageantStatus.CLOSED
        ) {
            throw new PageantAccessDeniedException(
                    "Can't delete! A pageant can only be deleted before or after starting.",
                    ErrorCode.PAGEANT_ACCESS_DENIED
            );
        }

        repository.deleteById(id);
    }
}
