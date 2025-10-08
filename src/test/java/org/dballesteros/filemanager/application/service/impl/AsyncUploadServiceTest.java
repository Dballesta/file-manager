package org.dballesteros.filemanager.application.service.impl;

import org.dballesteros.filemanager.application.service.AsyncUploadServiceImpl;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.port.client.UploadClientPort;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AsyncUploadServiceTest {

    @InjectMocks
    AsyncUploadServiceImpl asyncUploadService;
    @Mock
    private AssetRepositoryPort assetRepository;
    @Mock
    private UploadClientPort uploadClientPort;

    @Test
    void testUploadFileAsync() {
        final AssetDomain assetDomain = new AssetDomain();
        when(this.uploadClientPort.uploadFile(any(AssetDomain.class)))
                .thenReturn(Mono.just(assetDomain));
        when(this.assetRepository.update(any(AssetDomain.class)))
                .thenReturn(Mono.just(assetDomain));

        this.asyncUploadService.uploadFileAsync(assetDomain);

        verify(this.uploadClientPort).uploadFile(any(AssetDomain.class));
        verify(this.assetRepository).update(any(AssetDomain.class));

        verifyNoMoreInteractions(this.uploadClientPort, this.assetRepository);
    }
}