package org.dballesteros.filemanager.application.usecase;

import org.dballesteros.filemanager.domain.model.AssetDto;
import reactor.core.publisher.Mono;

public interface UploadAssetUseCase {
    Mono<AssetDto> upload(AssetDto assetDto);
}

