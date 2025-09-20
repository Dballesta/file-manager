package org.dballesteros.filemanager.infrastructure.controller.mapper;

import openapi.api.model.AssetFileUploadResponse;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AssetFileUploadResponseMapper {

    AssetFileUploadResponse toApiModel(AssetDto assetDto);
}
