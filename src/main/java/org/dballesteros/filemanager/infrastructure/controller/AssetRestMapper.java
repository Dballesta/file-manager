package org.dballesteros.filemanager.infrastructure.controller;

import openapi.api.model.Asset;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;
import java.util.function.Function;

@Mapper(componentModel = "spring")
public interface AssetRestMapper {

    @Mapping(target = "id", source = "id")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "uploadDate", source = "uploadDateStart", qualifiedByName = "instantToString")
    Asset toApiModel(AssetDto assetDto);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "filename", source = "filename")
    @Mapping(target = "contentType", source = "contentType")
    @Mapping(target = "url", source = "url")
    @Mapping(target = "size", source = "size")
    @Mapping(target = "uploadDateStart", source = "uploadDate", qualifiedByName = "stringToInstant")
    AssetDto toDomainModel(Asset asset);


    @Named("instantToString")
    static String instantToString(Instant instant) {
        return Objects.nonNull(instant)
                ? instant.toString()
                : null;
    }

    @Named("stringToInstant")
    static Instant stringToInstant(String value) {
        return Objects.nonNull(value)
                ? Instant.parse(value)
                : null;
    }
}

