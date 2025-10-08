package org.dballesteros.filemanager.domain.usecase;

import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.model.search.AssetFilterDomain;
import reactor.core.publisher.Flux;

public interface SearchAssetsUseCase {
    Flux<AssetDomain> search(AssetDomain assetDomain, AssetFilterDomain assetFilter);
}

