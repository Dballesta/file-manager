package org.dballesteros.filemanager.infrastructure.h2;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.dballesteros.filemanager.domain.model.search.AssetFilter;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.dballesteros.filemanager.infrastructure.h2.mapper.AssetEntityMapper;
import org.springframework.data.domain.Sort;
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

    private static Specification<AssetEntity> addAndLikeStartEndSpec(final Specification<AssetEntity> specification, final String rootKey, final String value) {
        return Optional.ofNullable(value)
                .map(val ->
                        specification.and((root, query, criteriaBuilder) ->
                                criteriaBuilder.like(root.get(rootKey), "%" + val + "%")))
                .orElse(specification);
    }

    private static Sort getAssetFilterSortByUploadDate(final AssetFilter assetFilter) {
        return Optional.ofNullable(assetFilter)
                .map(AssetFilter::getSortDirection)
                .map(sortDirection -> Sort.by(Sort.Direction.fromString(sortDirection.toString()), "uploadDateStart"))
                .orElse(Sort.by(Sort.Direction.DESC, "uploadDateStart"));
    }

    private static Specification<AssetEntity> getAssetEntitySpecification(final AssetDto ad) {
        Specification<AssetEntity> specification = Specification.allOf();
        specification = addAndLikeStartEndSpec(specification, "filename", ad.getFilename());
        specification = addAndLikeStartEndSpec(specification, "url", ad.getUrl());
        return specification;
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

    @Override
    public Flux<AssetDto> search(final AssetDto assetDto, final AssetFilter assetFilter) {
        return Mono.fromCallable(() -> Optional.ofNullable(assetDto)
                        .map(ad -> {
                            final Specification<AssetEntity> specification = getAssetEntitySpecification(ad);
                            final Sort sort = getAssetFilterSortByUploadDate(assetFilter);
                            return this.assetJpaRepository.findAll(specification, sort);
                        }).orElse(new ArrayList<>()))
                .flatMapMany(Flux::fromIterable)
                .map(AssetEntityMapper.INSTANCE::toDomain)
                .subscribeOn(Schedulers.boundedElastic());
    }
}
