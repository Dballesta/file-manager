package org.dballesteros.filemanager.domain.port.uploader;

import org.dballesteros.filemanager.domain.model.AssetDto;
import reactor.core.publisher.Mono;

public interface UploaderClient {
    Mono<Void> upload(AssetDto assetDto);
}
