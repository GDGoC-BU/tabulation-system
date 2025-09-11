package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.manager.ManagerSummaryDTO;
import com.michaelcanonizado.backend.models.Manager;
import com.michaelcanonizado.backend.models.ManagerRole;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-11T17:50:10+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class ManagerMapperImpl implements ManagerMapper {

    @Override
    public Manager toEntity(ManagerSummaryDTO managerSummaryDTO) {
        if ( managerSummaryDTO == null ) {
            return null;
        }

        String username = null;
        ManagerRole role = null;

        username = managerSummaryDTO.username();
        role = managerSummaryDTO.role();

        String passwordHash = null;

        Manager manager = new Manager( username, passwordHash, role );

        manager.setLastSeenAt( managerSummaryDTO.lastSeenAt() );

        return manager;
    }

    @Override
    public ManagerSummaryDTO toSummaryDTO(Manager manager) {
        if ( manager == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        ManagerRole role = null;
        LocalDateTime lastSeenAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = manager.getId();
        username = manager.getUsername();
        role = manager.getRole();
        lastSeenAt = manager.getLastSeenAt();
        createdAt = manager.getCreatedAt();
        updatedAt = manager.getUpdatedAt();

        boolean isOnline = false;

        ManagerSummaryDTO managerSummaryDTO = new ManagerSummaryDTO( id, username, isOnline, role, lastSeenAt, createdAt, updatedAt );

        return managerSummaryDTO;
    }
}
