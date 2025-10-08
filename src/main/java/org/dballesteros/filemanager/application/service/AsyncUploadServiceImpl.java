package org.dballesteros.filemanager.application.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.port.client.UploadClientPort;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.dballesteros.filemanager.domain.service.AsyncUploadService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class AsyncUploadServiceImpl implements AsyncUploadService {
    private final AssetRepositoryPort assetRepository;
    private final UploadClientPort uploadClientPort;


    @Override
    @Async("uploadExecutor")
    public void uploadFileAsync(final AssetDomain assetDomain) {
        this.uploadClientPort.uploadFile(assetDomain)
                .flatMap(this.assetRepository::update)
                .toFuture();
    }
}
