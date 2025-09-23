package org.dballesteros.filemanager.domain.port.client;

import org.dballesteros.filemanager.domain.model.AssetDto;
import reactor.core.publisher.Mono;

public interface UploadClientPort {
    Mono<Void> uploadFile(AssetDto assetDto);
}
