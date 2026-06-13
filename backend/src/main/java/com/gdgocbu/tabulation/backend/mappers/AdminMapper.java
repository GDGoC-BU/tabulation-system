package com.gdgocbu.tabulation.backend.mappers;

import com.gdgocbu.tabulation.backend.dtos.admin.AdminSummaryDTO;
import com.gdgocbu.tabulation.backend.models.Admin;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AdminMapper {
    Admin toEntity(AdminSummaryDTO adminSummaryDTO);
    AdminSummaryDTO toSummaryDTO(Admin admin);
}
