package org.dballesteros.filemanager.infrastructure.h2.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.AssetFilter;
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

@RequiredArgsConstructor
@Repository
@Transactional
public class AssetRepositoryAdapter implements AssetRepositoryPort {
    private final AssetJpaRepository assetJpaRepository;
    private final AssetEntityMapper assetEntityMapper;

    @Override
    public Mono<AssetDto> save(AssetDto asset) {
        return Mono.fromCallable(() -> {
            AssetEntity entity = assetEntityMapper.toEntity(asset);
            AssetEntity saved = assetJpaRepository.save(entity);
            return assetEntityMapper.toDomain(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Flux<AssetDto> search(AssetDto assetDto, AssetFilter assetFilter) {
        return Mono.fromCallable(() -> {
            Specification<AssetEntity> specification = Specification.allOf();
            // Add specifications based on assetDomain and assetFilter
            return Optional.ofNullable(assetDto)
                    .map(ad -> {
                        Optional.ofNullable(ad.getFilename())
                                .ifPresent(filename ->
                                        specification.and((root, query, criteriaBuilder) ->
                                        criteriaBuilder.like(root.get("name"), "%" + filename + "%")));
                        Optional.ofNullable(ad.getUrl())
                                .ifPresent(url -> specification.and((root, query, criteriaBuilder) ->
                                        criteriaBuilder.like(root.get("url"), "%" + url + "%")));
                        return assetJpaRepository.findAll(specification);
                    }).orElse(new ArrayList<>());
        })
                .flatMapMany(Flux::fromIterable)
                .map(assetEntityMapper::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }


}
