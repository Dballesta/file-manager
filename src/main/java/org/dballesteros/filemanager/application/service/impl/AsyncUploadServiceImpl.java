package org.dballesteros.filemanager.application.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.application.service.AsyncUploadService;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.port.client.UploadClientPort;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class AsyncUploadServiceImpl implements AsyncUploadService {
    private final AssetRepositoryPort assetRepository;
    private final UploadClientPort uploadClientPort;


    @Async("uploadExecutor")
    public void uploadFileAsync(final AssetDto assetDto) {
        this.uploadClientPort.uploadFile(assetDto)
                .flatMap(this.assetRepository::update)
                .toFuture();
    }
}
