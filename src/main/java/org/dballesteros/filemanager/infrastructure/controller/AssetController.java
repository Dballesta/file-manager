package org.dballesteros.filemanager.infrastructure.controller;

import lombok.RequiredArgsConstructor;
import openapi.api.ApiFileManager;
import openapi.api.model.Asset;
import openapi.api.model.AssetFileUploadRequest;
import openapi.api.model.AssetFileUploadResponse;
import openapi.api.model.SortDirectionEnum;
import org.dballesteros.filemanager.application.usecase.SearchAssetsUseCase;
import org.dballesteros.filemanager.application.usecase.UploadAssetUseCase;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.search.AssetFilter;
import org.dballesteros.filemanager.domain.model.search.SortDirection;
import org.dballesteros.filemanager.domain.util.TimeUtil;
import org.dballesteros.filemanager.infrastructure.controller.mapper.AssetFileUploadRequestMapper;
import org.dballesteros.filemanager.infrastructure.controller.mapper.AssetFileUploadResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AssetController implements ApiFileManager {
    private final SearchAssetsUseCase searchAssetsUseCase;
    private final UploadAssetUseCase uploadAssetUseCase;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Asset> getAssetsByFilter(final String uploadDateStart, final String uploadDateEnd, final String filename, final String filetype, final SortDirectionEnum sortDirection, final ServerWebExchange exchange) {
        final AssetDto assetDto = AssetDto.builder()
                .uploadDateStart(TimeUtil.stringToInstant(uploadDateStart))
                .uploadDateEnd(TimeUtil.stringToInstant(uploadDateEnd))
                .filename(filename)
                .contentType(filetype)
                .build();

        final AssetFilter assetFilter = AssetFilter.builder()
                .sortDirection(Optional.ofNullable(sortDirection)
                        .map(SortDirectionEnum::getValue)
                        .map(SortDirection::fromValue)
                        .orElse(null))
                .build();

        return this.searchAssetsUseCase.search(assetDto, assetFilter)
                .map(AssetRestMapper.INSTANCE::toApiModel);
    }

    @Override
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Mono<AssetFileUploadResponse> uploadAssetFile(final Mono<AssetFileUploadRequest> assetFileUploadRequest, final ServerWebExchange exchange) {
        return assetFileUploadRequest
                .map(AssetFileUploadRequestMapper.INSTANCE::toDomain)
                .flatMap(this.uploadAssetUseCase::upload)
                .map(AssetFileUploadResponseMapper.INSTANCE::toApiModel);
    }
}
