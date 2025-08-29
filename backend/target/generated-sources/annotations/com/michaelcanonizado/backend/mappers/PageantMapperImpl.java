package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.pageant.PageantCreateDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantSummaryDTO;
import com.michaelcanonizado.backend.dtos.pageant.PageantUpdateDTO;
import com.michaelcanonizado.backend.models.Pageant;
import com.michaelcanonizado.backend.models.PageantStatus;
import java.time.Instant;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-08-29T22:27:26+0800",
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
        Instant createdAt = null;
        Instant updatedAt = null;

        id = pageant.getId();
        title = pageant.getTitle();
        status = pageant.getStatus();
        createdAt = pageant.getCreatedAt();
        updatedAt = pageant.getUpdatedAt();

        PageantSummaryDTO pageantSummaryDTO = new PageantSummaryDTO( id, title, status, createdAt, updatedAt );

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
