package org.dballesteros.filemanager.domain.port.client;

import org.dballesteros.filemanager.domain.model.AssetDomain;
import reactor.core.publisher.Mono;

public interface UploadClientPort {
    Mono<AssetDomain> uploadFile(AssetDomain assetDomain);
}
