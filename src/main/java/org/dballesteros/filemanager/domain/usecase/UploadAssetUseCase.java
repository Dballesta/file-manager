package org.dballesteros.filemanager.domain.usecase;

import org.dballesteros.filemanager.domain.model.AssetDomain;
import reactor.core.publisher.Mono;

public interface UploadAssetUseCase {
    Mono<AssetDomain> upload(AssetDomain assetDomain);
}

