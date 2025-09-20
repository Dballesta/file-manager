package org.dballesteros.filemanager.domain.port.repository;

import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.AssetFilter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AssetRepositoryPort {

    Mono<AssetDto> save(AssetDto asset);

    Flux<AssetDto> search(AssetDto assetDto, AssetFilter assetFilter);

    Mono<AssetDto> update(AssetDto assetDto);
}

