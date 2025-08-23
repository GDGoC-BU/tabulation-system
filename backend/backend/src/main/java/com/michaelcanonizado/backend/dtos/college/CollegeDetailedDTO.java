package com.michaelcanonizado.backend.dtos.college;

import com.michaelcanonizado.backend.models.Candidate;

import java.util.List;
import java.util.UUID;

public record CollegeDetailedDTO(UUID id, String code, String name, List<Candidate> candidates) {
}
