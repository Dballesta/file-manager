package org.dballesteros.filemanager.infrastructure.h2.mapper;

import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import java.util.Optional;
import java.util.UUID;

@Mapper
public interface AssetEntityMapper {

    AssetEntityMapper INSTANCE = Mappers.getMapper(AssetEntityMapper.class);

    @Mapping(target = "uuid", source = "id", qualifiedByName = "stringToUuid")
    AssetEntity toEntity(AssetDomain asset);

    @Mapping(target = "encodedFile", ignore = true)
    @Mapping(target = "id", source = "uuid", qualifiedByName = "uuidToString")
    AssetDomain toDomain(AssetEntity entity);

    @Named("uuidToString")
    default String uuidToString(final UUID uuid) {
        return Optional.ofNullable(uuid)
                .map(Object::toString)
                .orElse(null);
    }

    @Named("stringToUuid")
    default UUID stringToUuid(final String uuidString) {
        return Optional.ofNullable(uuidString)
                .map(UUID::fromString)
                .orElse(null);
    }
}
