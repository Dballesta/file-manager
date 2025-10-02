package org.dballesteros.filemanager.application.service;

import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.search.AssetFilter;
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
class SearchAssetServiceTest {

    @Mock
    private AssetRepositoryPort assetRepository;

    @InjectMocks
    private SearchAssetService searchAssetService;

    @Test
    void testSearch() {
        final AssetDto assetDto = new AssetDto();
        final AssetFilter assetFilter = AssetFilter.builder().build();


        when(this.assetRepository.search(any(AssetDto.class), any(AssetFilter.class))).thenReturn(Flux.just(assetDto));

        StepVerifier.create(this.searchAssetService.search(assetDto, assetFilter))
                .expectNextCount(1)
                .verifyComplete();

        verify(this.assetRepository).search(any(AssetDto.class), any(AssetFilter.class));

        verifyNoMoreInteractions(this.assetRepository);
    }
}