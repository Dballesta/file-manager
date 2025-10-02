package org.dballesteros.filemanager.domain.port.client;

import org.dballesteros.filemanager.domain.model.AssetDto;
import reactor.core.publisher.Mono;

public interface UploadClientPort {
    Mono<AssetDto> uploadFile(AssetDto assetDto);
}
