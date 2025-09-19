package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-19T15:40:55+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 21.0.7 (Oracle Corporation)"
)
@Component
public class PageantMapperImpl implements PageantMapper {

    @Override
    public Pageant toEntity(PageantCreateDTO pageantCreateDTO) {
        if ( pageantCreateDTO == null ) {
            return null;
        }

        String title = null;

        title = pageantCreateDTO.title();

        Pageant pageant = new Pageant( title );

        return pageant;
    }

    @Override
    public Pageant toEntity(PageantSummaryDTO pageantSummaryDTO) {
        if ( pageantSummaryDTO == null ) {
            return null;
        }

        String title = null;

        title = pageantSummaryDTO.title();

        Pageant pageant = new Pageant( title );

        pageant.setStatus( pageantSummaryDTO.status() );
        pageant.setStartedAt( pageantSummaryDTO.startedAt() );
        pageant.setEndedAt( pageantSummaryDTO.endedAt() );

        return pageant;
    }

    @Override
    public PageantSummaryDTO toSummary(Pageant pageant) {
        if ( pageant == null ) {
            return null;
        }

        UUID id = null;
        String title = null;
        PageantStatus status = null;
        LocalDateTime startedAt = null;
        LocalDateTime endedAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        id = pageant.getId();
        title = pageant.getTitle();
        status = pageant.getStatus();
        startedAt = pageant.getStartedAt();
        endedAt = pageant.getEndedAt();
        createdAt = pageant.getCreatedAt();
        updatedAt = pageant.getUpdatedAt();

        PageantSummaryDTO pageantSummaryDTO = new PageantSummaryDTO( id, title, status, startedAt, endedAt, createdAt, updatedAt );

        return pageantSummaryDTO;
    }

    @Override
    public void updateEntityFromDTO(Pageant pageant, PageantUpdateDTO pageantUpdateDTO) {
        if ( pageantUpdateDTO == null ) {
            return;
        }

        pageant.setStatus( pageantUpdateDTO.status() );
        pageant.setTitle( pageantUpdateDTO.title() );
    }
}
