package org.dballesteros.filemanager.infrastructure.h2.mapper;

import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AssetEntityMapper {
    AssetEntityMapper INSTANCE = Mappers.getMapper(AssetEntityMapper.class);

    @Mapping(target = "idClient", source = "id")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "uploadDateStart", source = "uploadDateStart")
    @Mapping(target = "uploadDateEnd", source = "uploadDateEnd")
    AssetEntity toEntity(AssetDto asset);

    @Mapping(target = "id", source = "idClient")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "uploadDateStart", source = "uploadDateStart")
    @Mapping(target = "uploadDateEnd", source = "uploadDateEnd")
    AssetDto toDomain(AssetEntity entity);
}
