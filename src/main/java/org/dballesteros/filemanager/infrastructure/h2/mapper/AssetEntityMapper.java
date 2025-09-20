package org.dballesteros.filemanager.infrastructure.h2.mapper;

import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Objects;
import java.util.UUID;

@Mapper(componentModel = "spring")
public interface AssetEntityMapper {

    @Mapping(target = "uuid", source = "id" , qualifiedByName = "stringToUuid")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "uploadDateStart", source = "uploadDateStart")
    @Mapping(target = "uploadDateEnd", source = "uploadDateEnd")
    AssetEntity toEntity(AssetDto asset);

    @Mapping(target = "id", source = "uuid", qualifiedByName = "uuidToString")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "uploadDateStart", source = "uploadDateStart")
    @Mapping(target = "uploadDateEnd", source = "uploadDateEnd")
    AssetDto toDomain(AssetEntity entity);

    @Named("uuidToString")
    default String uuidToString(UUID uuid) {
        return Objects.nonNull(uuid)
                ? uuid.toString()
                : null;
    }

    @Named("stringToUuid")
    default UUID stringToUuid(String uuidString) {
        return Objects.nonNull(uuidString)
                ? UUID.fromString(uuidString)
                : null;
    }
}
