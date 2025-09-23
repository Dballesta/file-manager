package org.dballesteros.filemanager.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.application.usecase.UploadAsyncUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.port.client.UploadClientPort;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.infrastructure.config.FilebinConfigProps;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;

@RequiredArgsConstructor
@Service
@Slf4j
public class AsyncUploadService implements UploadAsyncUseCase {
    private final AssetRepositoryPort assetRepository;
    private final UploadClientPort uploadClientPort;
    private final FilebinConfigProps filebinConfigProps;

    private static AssetDto fillDataCompletionAssetDto(final AssetDto assetDto, final String baseUrl, final String binId) {
        assetDto.setUploadDateEnd(Instant.now());
        assetDto.setUrl(String.join("/", baseUrl, binId, assetDto.getFilename()));
        return assetDto;
    }


    @Async("uploadExecutor")
    @Override
    public void uploadFileAsync(final AssetDto assetDto) {
        this.uploadClientPort.uploadFile(assetDto)
                .then(this.assetRepository.update(AsyncUploadService.fillDataCompletionAssetDto(assetDto, this.filebinConfigProps.getBaseUrl(), this.filebinConfigProps.getBinId())))
                .then()
                .toFuture();
    }
}
