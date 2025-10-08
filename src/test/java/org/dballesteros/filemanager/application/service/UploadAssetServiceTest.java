package org.dballesteros.filemanager.application.service;

import org.dballesteros.filemanager.application.usecase.UploadAssetService;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UploadAssetServiceTest {

    @InjectMocks
    private UploadAssetService uploadAssetService;
    @Mock
    private AssetRepositoryPort assetRepository;
    @Mock
    private AsyncUploadServiceImpl asyncUploadService;


    @Test
    void testUpload() {
        final AssetDomain assetDomain = AssetDomain.builder().build();

        when(this.assetRepository.save(any(AssetDomain.class)))
                .thenReturn(Mono.just(assetDomain));
        doNothing().when(this.asyncUploadService).uploadFileAsync(any(AssetDomain.class));

        StepVerifier.create(this.uploadAssetService.upload(assetDomain))
                .expectNextCount(1)
                .verifyComplete();

        verify(this.assetRepository).save(any(AssetDomain.class));
        verify(this.asyncUploadService).uploadFileAsync(any(AssetDomain.class));

        verifyNoMoreInteractions(this.assetRepository, this.asyncUploadService);
    }
}