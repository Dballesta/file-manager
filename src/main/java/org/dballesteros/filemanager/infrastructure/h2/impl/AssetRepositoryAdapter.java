package org.dballesteros.filemanager.infrastructure.h2.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.dballesteros.filemanager.domain.model.search.AssetFilter;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.infrastructure.h2.AssetJpaRepository;
import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.dballesteros.filemanager.infrastructure.h2.mapper.AssetEntityMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
@Transactional
public class AssetRepositoryAdapter implements AssetRepositoryPort {
    private final AssetJpaRepository assetJpaRepository;

    private static void addAndLikeStartEndSpec(final Specification<AssetEntity> specification, final String rootKey, final String value) {
        Optional.ofNullable(value)
                .ifPresent(val ->
                        specification.and((root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get(rootKey), "%%%s%%".formatted(val))));
    }


    @Override
    public Mono<AssetDto> save(final AssetDto asset) {
        return Mono.fromCallable(() -> {
            final AssetEntity entity = AssetEntityMapper.INSTANCE.toEntity(asset);
            final AssetEntity saved = this.assetJpaRepository.save(entity);
            return AssetEntityMapper.INSTANCE.toDomain(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AssetDto> update(final AssetDto asset) {
        return Mono.fromCallable(() ->
                        this.assetJpaRepository.findByUuid(UUID.fromString((asset.getId())))
                                .map(assetEntity -> {
                                    assetEntity.setUploadDateEnd(asset.getUploadDateEnd());
                                    assetEntity.setUrl(asset.getUrl());
                                    return this.assetJpaRepository.save(assetEntity);
                                })
                                .map(AssetEntityMapper.INSTANCE::toDomain)
                                .orElseThrow(() -> new ExceptionDetail("UPDATE_EXCEPTION", "Asset not found with id: %s".formatted(asset.getId()))))
                .subscribeOn(Schedulers.boundedElastic());
    }

    //ExceptionDetail.of("UPDATE_EXCEPTION", "Asset not found with id: " + asset.getId())
    @Override
    public Flux<AssetDto> search(final AssetDto assetDto, final AssetFilter assetFilter) {
        return Mono.fromCallable(() -> {
                    final Specification<AssetEntity> specification = Specification.allOf();
                    // Add specifications based on assetDomain and assetFilter
                    return Optional.ofNullable(assetDto)
                            .map(ad -> {
                                addAndLikeStartEndSpec(specification, "filename", ad.getFilename());
                                addAndLikeStartEndSpec(specification, "url", ad.getUrl());
                                return this.assetJpaRepository.findAll(specification);
                            }).orElse(new ArrayList<>());
                })
                .flatMapMany(Flux::fromIterable)
                .map(AssetEntityMapper.INSTANCE::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
