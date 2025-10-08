package org.dballesteros.filemanager.application.usecase;

import lombok.RequiredArgsConstructor;
import org.dballesteros.filemanager.domain.usecase.SearchAssetsUseCase;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.model.search.AssetFilterDomain;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class SearchAssetUseCaseImpl implements SearchAssetsUseCase {
    private final AssetRepositoryPort assetRepository;

    @Override
    public Flux<AssetDomain> search(final AssetDomain assetDomain, final AssetFilterDomain assetFilter) {
        return this.assetRepository.search(assetDomain, assetFilter);
    }
}
