package org.dballesteros.filemanager.application.service;

import lombok.RequiredArgsConstructor;
import openapi.client.api.FileApi;
import org.dballesteros.filemanager.application.usecase.UploadAssetUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.infrastructure.config.FilebinConfigProps;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class UploadAssetService implements UploadAssetUseCase {
    private final AssetRepositoryPort assetRepository;
    private final FileApi fileApi;
    private final FilebinConfigProps filebinConfigProps;


    @Override
    public Mono<AssetDto> upload(AssetDto assetDto) {
        assetDto.setUploadDateStart(Instant.now());

        return assetRepository.save(assetDto)
                .flatMap(savedAssetDto ->
                        fileApi.binFilenamePost(
                                filebinConfigProps.getBinId(),
                                savedAssetDto.getFilename(),
                                filebinConfigProps.getCid(),
                                Base64.getEncoder().encodeToString(assetDto.getEncodedFile())
                        ).thenReturn(savedAssetDto))
                .map(ad -> {
                    ad.setUploadDateEnd(Instant.now());
                    ad.setUrl(new StringBuilder(filebinConfigProps.getBaseUrl())
                            .append("/")
                            .append(filebinConfigProps.getBinId())
                            .append("/")
                            .append(ad.getFilename())
                            .toString());
                    return ad;
                })
                .flatMap(assetRepository::update);


    }
}
