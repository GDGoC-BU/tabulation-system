package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantStatusDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.models.Pageant;
import java.time.LocalDateTime;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-09-22T22:53:28+0800",
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
    public PageantSummaryDTO toSummary(Pageant pageant) {
        if ( pageant == null ) {
            return null;
        }

        PageantStatusDTO status = null;
        UUID id = null;
        String title = null;
        LocalDateTime startedAt = null;
        LocalDateTime endedAt = null;
        LocalDateTime createdAt = null;
        LocalDateTime updatedAt = null;

        status = mapStatus( pageant.getStatus() );
        id = pageant.getId();
        title = pageant.getTitle();
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

        pageant.setTitle( pageantUpdateDTO.title() );
    }
}
