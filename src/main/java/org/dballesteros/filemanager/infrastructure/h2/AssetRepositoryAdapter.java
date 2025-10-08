package org.dballesteros.filemanager.infrastructure.h2;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.dballesteros.filemanager.domain.model.search.AssetFilterDomain;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.dballesteros.filemanager.infrastructure.h2.mapper.AssetEntityMapper;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
@Slf4j
public class AssetRepositoryAdapter implements AssetRepositoryPort {
    private final AssetJpaRepository assetJpaRepository;

    private static Specification<AssetEntity> addAndLikeStartEndSpec(Specification<AssetEntity> specification, String rootKey, String value) {
        return Optional.ofNullable(value)
                .map(val ->
                        specification.and((root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get(rootKey), MessageFormat.format("%{0}%", val))))
                .orElse(specification);
    }

    private static Sort getAssetFilterSortByUploadDate(AssetFilterDomain assetFilter) {
        return Optional.ofNullable(assetFilter)
                .map(AssetFilterDomain::getSortDirection)
                .map(sortDirection -> Sort.by(Sort.Direction.fromString(sortDirection.toString()), "uploadDateStart"))
                .orElse(Sort.by(Sort.Direction.DESC, "uploadDateStart"));
    }

    private static Specification<AssetEntity> getAssetEntitySpecification(AssetDomain ad) {
        Specification<AssetEntity> specification = Specification.allOf();
        specification = addAndLikeStartEndSpec(specification, "filename", ad.getFilename());
        specification = addAndLikeStartEndSpec(specification, "url", ad.getUrl());
        specification = addAndLikeStartEndSpec(specification, "contentType", ad.getContentType());
        return specification;
    }

    @Override
    public Mono<AssetDomain> save(AssetDomain asset) {
        return Mono.fromCallable(() -> {
            AssetEntity entity = AssetEntityMapper.INSTANCE.toEntity(asset);
            AssetEntity saved = this.assetJpaRepository.save(entity);
            log.trace("Saving asset in DB: {}", asset);
            return AssetEntityMapper.INSTANCE.toDomain(saved);
        }).subscribeOn(Schedulers.boundedElastic());
    }

    @Override
    public Mono<AssetDomain> update(AssetDomain asset) {
        return Mono.fromCallable(() ->
                        this.assetJpaRepository.findByUuid(UUID.fromString((asset.getId())))
                                .map(assetEntity -> {
                                    assetEntity.setUploadDateEnd(asset.getUploadDateEnd());
                                    assetEntity.setUrl(asset.getUrl());
                                    log.trace("Updating asset in DB: {}", asset);
                                    return this.assetJpaRepository.save(assetEntity);
                                })
                                .map(AssetEntityMapper.INSTANCE::toDomain)
                                .orElseThrow(() -> new ExceptionDetail("UPDATE_DB_EXCEPTION", "Asset not found with id: %s".formatted(asset.getId()))))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(assetEntity -> log.trace("Saved asset in DB: {}", asset.toString()))
                .onErrorResume(throwable -> Mono.error(new ExceptionDetail("UPDATE_DB_EXCEPTION", "Error updating asset with id: %s in DB".formatted(asset.getId()), throwable)));
    }

    @Override
    public Flux<AssetDomain> search(AssetDomain assetDomain, AssetFilterDomain assetFilter) {
        return Mono.fromCallable(() -> Optional.ofNullable(assetDomain)
                        .map(ad -> {
                            Specification<AssetEntity> specification = getAssetEntitySpecification(ad);
                            Sort sort = getAssetFilterSortByUploadDate(assetFilter);
                            log.trace("Searching asset in DB: {}", specification);
                            return this.assetJpaRepository.findAll(specification, sort);
                        }).orElse(new ArrayList<>()))
                .subscribeOn(Schedulers.boundedElastic())
                .doOnSuccess(assetEntity -> log.trace("Searched asset in DB: {}", assetEntity))
                .flatMapMany(Flux::fromIterable)
                .map(AssetEntityMapper.INSTANCE::toDomain)
                .onErrorResume(throwable -> Mono.error(new ExceptionDetail("SEARCH_DB_EXCEPTION", "Error searching asset in DB", throwable)));
    }
}
