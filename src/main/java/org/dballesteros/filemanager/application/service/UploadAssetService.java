package org.dballesteros.filemanager.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.application.usecase.UploadAssetUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
@RequiredArgsConstructor
public class UploadAssetService implements UploadAssetUseCase {
    private final AssetRepositoryPort assetRepository;
    private final AsyncUploadService asyncUploadService;

    @Override
    public Mono<AssetDto> upload(final AssetDto assetDto) {
        return this.assetRepository.save(assetDto)
                .doOnSuccess(assetDto1 -> this.asyncUploadService.uploadFileAsync(assetDto));
    }
}
