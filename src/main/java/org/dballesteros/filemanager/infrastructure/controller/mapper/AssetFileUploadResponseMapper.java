package org.dballesteros.filemanager.infrastructure.controller.mapper;

import openapi.api.model.AssetFileUploadResponse;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AssetFileUploadResponseMapper {

    AssetFileUploadResponseMapper INSTANCE = Mappers.getMapper(AssetFileUploadResponseMapper.class);

    AssetFileUploadResponse toApiModel(AssetDto assetDto);
}
