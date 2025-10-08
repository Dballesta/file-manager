package org.dballesteros.filemanager.infrastructure.controller;

import openapi.api.model.AssetFileUploadRequest;
import org.dballesteros.filemanager.domain.usecase.SearchAssetsUseCase;
import org.dballesteros.filemanager.domain.usecase.UploadAssetUseCase;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.model.search.AssetFilterDomain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class AssetControllerTest {

    @Mock
    private SearchAssetsUseCase searchAssetsUseCase;

    @Mock
    private UploadAssetUseCase uploadAssetUseCase;

    @InjectMocks
    private AssetController assetController;

    @Test
    void testGetAssetsByFilter() {
        when(this.searchAssetsUseCase.search(any(AssetDomain.class), any(AssetFilterDomain.class)))
                .thenReturn(
                        Flux.just(AssetDomain.builder().id("1").filename("file1.txt").build(),
                                AssetDomain.builder().id("2").filename("file2.txt").build()));


        StepVerifier.create(this.assetController.getAssetsByFilter(null, null, "file", null, null, null))
                .expectNextCount(2)
                .verifyComplete();

        verify(this.searchAssetsUseCase).search(any(AssetDomain.class), any(AssetFilterDomain.class));
    }

    @Test
    void testUploadAssetFile() {
        final AssetFileUploadRequest requestBody = new AssetFileUploadRequest();
        final AssetDomain assetDomain = AssetDomain.builder().id("1").filename("file1.txt").build();

        when(this.uploadAssetUseCase.upload(any(AssetDomain.class)))
                .thenReturn(Mono.just(assetDomain));

        StepVerifier.create(this.assetController.uploadAssetFile(Mono.just(requestBody), null))
                .expectNextCount(1)
                .verifyComplete();

        verify(this.uploadAssetUseCase).upload(any(AssetDomain.class));
    }
}