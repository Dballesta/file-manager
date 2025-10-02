package org.dballesteros.filemanager.application.service;

import lombok.RequiredArgsConstructor;
import org.dballesteros.filemanager.application.usecase.SearchAssetsUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.search.AssetFilter;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class SearchAssetService implements SearchAssetsUseCase {
    private final AssetRepositoryPort assetRepository;

    @Override
    public Flux<AssetDto> search(final AssetDto assetDto, final AssetFilter assetFilter) {
        return this.assetRepository.search(assetDto, assetFilter);
    }
}
