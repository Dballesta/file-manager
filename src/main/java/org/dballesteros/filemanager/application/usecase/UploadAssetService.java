package org.dballesteros.filemanager.application.usecase;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.domain.service.AsyncUploadService;
import org.dballesteros.filemanager.domain.usecase.UploadAssetUseCase;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadAssetService implements UploadAssetUseCase {
    private final AssetRepositoryPort assetRepository;
    private final AsyncUploadService asyncUploadService;

    @Override
    public Mono<AssetDomain> upload(AssetDomain assetDomain) {
        return this.assetRepository.save(assetDomain)
                .map(asset -> {
                    assetDomain.setId(asset.getId());
                    return assetDomain;
                })
                .doOnSuccess(this.asyncUploadService::uploadFileAsync);
    }
}
