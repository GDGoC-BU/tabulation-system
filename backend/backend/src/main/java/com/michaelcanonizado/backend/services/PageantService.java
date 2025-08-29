package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.mappers.PageantMapper;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.repositories.PageantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PageantService {
    @Autowired
    private PageantRepository repository;

    @Autowired
    private PageantMapper mapper;

    public PageantSummaryDTO addPageant(PageantCreateDTO pageantCreateDTO) {
        Pageant pageant = repository.save(mapper.toEntity(pageantCreateDTO));
        return mapper.toSummary(pageant);
    }
}
