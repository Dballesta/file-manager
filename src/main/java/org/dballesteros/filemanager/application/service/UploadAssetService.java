package org.dballesteros.filemanager.application.service;

import org.dballesteros.filemanager.application.usecase.UploadAssetUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
public class UploadAssetService implements UploadAssetUseCase {
    private final AssetRepositoryPort assetRepository;

    public UploadAssetService(AssetRepositoryPort assetRepository) {
        this.assetRepository = assetRepository;
    }


    @Override
    public Mono<AssetDto> upload(AssetDto assetDto) {
        return null;
    }
}
