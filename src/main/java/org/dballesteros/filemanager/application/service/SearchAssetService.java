package org.dballesteros.filemanager.application.service;

import org.dballesteros.filemanager.application.usecase.SearchAssetsUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.AssetFilter;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class SearchAssetService implements SearchAssetsUseCase {
    private final AssetRepositoryPort assetRepository;

    public SearchAssetService(AssetRepositoryPort assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Flux<AssetDto> search(AssetDto assetDto, AssetFilter assetFilter) {
        return assetRepository.search(assetDto, assetFilter);
    }
}
