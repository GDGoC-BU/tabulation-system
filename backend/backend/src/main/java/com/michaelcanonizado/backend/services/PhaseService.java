package com.michaelcanonizado.backend.services;

import com.michaelcanonizado.backend.exceptions.common.ErrorCode;
import com.michaelcanonizado.backend.exceptions.customs.EntityNotFoundException;
import com.michaelcanonizado.backend.models.College;
import com.michaelcanonizado.backend.models.Phase;
import com.michaelcanonizado.backend.repositories.PhaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class PhaseService {
    @Autowired
    private PhaseRepository phaseRepository;

    /* Mapstruct resolver method */
    public Phase findById(UUID id) {
        return phaseRepository.findById(id).orElseThrow(() -> {
            return new EntityNotFoundException("Phase not found!", ErrorCode.ENTITY_NOT_FOUND);
        });
    }
}
