package org.dballesteros.filemanager.application.service;

import org.dballesteros.filemanager.application.usecase.SearchAssetUseCaseImpl;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.model.search.AssetFilterDomain;
import org.dballesteros.filemanager.domain.port.repository.AssetRepositoryPort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SearchAssetUseCaseImplTest {

    @Mock
    private AssetRepositoryPort assetRepository;

    @InjectMocks
    private SearchAssetUseCaseImpl searchAssetUseCaseImpl;

    @Test
    void testSearch() {
        final AssetDomain assetDomain = new AssetDomain();
        final AssetFilterDomain assetFilter = AssetFilterDomain.builder().build();


        when(this.assetRepository.search(any(AssetDomain.class), any(AssetFilterDomain.class))).thenReturn(Flux.just(assetDomain));

        StepVerifier.create(this.searchAssetUseCaseImpl.search(assetDomain, assetFilter))
                .expectNextCount(1)
                .verifyComplete();

        verify(this.assetRepository).search(any(AssetDomain.class), any(AssetFilterDomain.class));

        verifyNoMoreInteractions(this.assetRepository);
    }
}