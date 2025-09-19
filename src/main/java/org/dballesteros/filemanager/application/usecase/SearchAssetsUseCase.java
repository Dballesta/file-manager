package org.dballesteros.filemanager.application.usecase;

import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.AssetFilter;
import reactor.core.publisher.Flux;

public interface SearchAssetsUseCase {
    Flux<AssetDto> search(AssetDto assetDto, AssetFilter assetFilter);
}

