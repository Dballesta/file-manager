package org.dballesteros.filemanager.infrastructure.controller;

import openapi.api.model.Asset;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.time.Instant;
import java.util.Objects;

@Mapper
public interface AssetRestMapper {

    AssetRestMapper INSTANCE = Mappers.getMapper(AssetRestMapper.class);

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

    @Mapping(target = "uploadDate", source = "uploadDateStart", qualifiedByName = "instantToString")
    Asset toApiModel(AssetDomain assetDomain);
}

