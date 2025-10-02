package org.dballesteros.filemanager.application.service.impl;

import org.dballesteros.filemanager.domain.model.AssetDto;
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
        final AssetDto assetDto = new AssetDto();
        when(this.uploadClientPort.uploadFile(any(AssetDto.class)))
                .thenReturn(Mono.just(assetDto));
        when(this.assetRepository.update(any(AssetDto.class)))
                .thenReturn(Mono.just(assetDto));

        this.asyncUploadService.uploadFileAsync(assetDto);

        verify(this.uploadClientPort).uploadFile(any(AssetDto.class));
        verify(this.assetRepository).update(any(AssetDto.class));

        verifyNoMoreInteractions(this.uploadClientPort, this.assetRepository);
    }
}