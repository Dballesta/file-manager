package org.dballesteros.filemanager.infrastructure.controller.mapper;

import openapi.api.model.AssetFileUploadRequest;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetFileUploadRequestMapper {

    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "encodedFile", source = "encodedFile")
    @Mapping(target = "contentType", source = "contentType")
    AssetDto toDomain(AssetFileUploadRequest request);
}
