package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.manager.ManagerSummaryDTO;
import com.michaelcanonizado.backend.models.Manager;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ManagerMapper {
    Manager toEntity(ManagerSummaryDTO managerSummaryDTO);
    ManagerSummaryDTO toSummaryDTO(Manager manager);
}
