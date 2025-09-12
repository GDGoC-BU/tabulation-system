package com.michaelcanonizado.backend.mappers;

import com.michaelcanonizado.backend.dtos.admin.AdminSummaryDTO;
import com.michaelcanonizado.backend.models.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminSummaryDTO adminSummaryDTO);
    AdminSummaryDTO toSummaryDTO(Admin admin);
}
