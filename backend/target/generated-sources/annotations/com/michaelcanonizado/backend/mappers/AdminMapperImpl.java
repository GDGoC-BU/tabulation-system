package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.admin.AdminSummaryDTO;
import com.michaelcanonizado.backend.models.Admin;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T17:28:51+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class AdminMapperImpl implements AdminMapper {

    @Override
    public Admin toEntity(AdminSummaryDTO adminSummaryDTO) {
        if ( adminSummaryDTO == null ) {
            return null;
        }

        String username = null;

        username = adminSummaryDTO.username();

        String passwordHash = null;

        Admin admin = new Admin( username, passwordHash );

        admin.setLastSeenAt( adminSummaryDTO.lastSeenAt() );

        return admin;
    }

    @Override
    public AdminSummaryDTO toSummaryDTO(Admin admin) {
        if ( admin == null ) {
            return null;
        }

        UUID id = null;
        String username = null;
        LocalDateTime lastSeenAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = admin.getId();
        username = admin.getUsername();
        lastSeenAt = admin.getLastSeenAt();
        createdAt = admin.getCreatedAt();
        updatedAt = admin.getUpdatedAt();

        boolean isOnline = false;

        AdminSummaryDTO adminSummaryDTO = new AdminSummaryDTO( id, username, isOnline, lastSeenAt, createdAt, updatedAt );

        return adminSummaryDTO;
    }
}
