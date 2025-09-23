package org.dballesteros.filemanager.infrastructure.controller.mapper;

import openapi.api.model.AssetFileUploadRequest;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssetFileUploadRequestMapper {

    AssetFileUploadRequestMapper INSTANCE = Mappers.getMapper(AssetFileUploadRequestMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "size", ignore = true)
    @Mapping(target = "uploadDateEnd", ignore = true)
    @Mapping(target = "uploadDateStart", ignore = true)
    @Mapping(target = "url", ignore = true)
    AssetDto toDomain(AssetFileUploadRequest request);
}
