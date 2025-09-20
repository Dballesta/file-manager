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
import org.dballesteros.filemanager.domain.model.AssetFilter;
import org.dballesteros.filemanager.domain.model.SortDirection;
import org.dballesteros.filemanager.domain.util.TimeUtil;
import org.dballesteros.filemanager.infrastructure.controller.mapper.AssetFileUploadRequestMapper;
import org.dballesteros.filemanager.infrastructure.controller.mapper.AssetFileUploadResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
public class AssetController implements ApiFileManager {
    private final SearchAssetsUseCase searchAssetsUseCase;
    private final UploadAssetUseCase uploadAssetUseCase;
    private final AssetFileUploadRequestMapper assetFileUploadRequestMapper;
    private final AssetFileUploadResponseMapper assetFileUploadResponseMapper;
    private final AssetRestMapper assetRestMapper;

    @Override
    @ResponseStatus(HttpStatus.CREATED)
    public Flux<Asset> getAssetsByFilter(String uploadDateStart, String uploadDateEnd, String filename, String filetype, SortDirectionEnum sortDirection, ServerWebExchange exchange) {
        return searchAssetsUseCase.search(new AssetDto(TimeUtil.stringToInstant(uploadDateStart),
                                TimeUtil.stringToInstant(uploadDateEnd),
                                filename, filetype),
                new AssetFilter(Optional.ofNullable(sortDirection)
                        .map(sd -> SortDirection.fromValue(null))
                        .orElse(null)))
                .map(assetRestMapper::toApiModel);
    }

    @Override
    @ResponseStatus(HttpStatus.OK)
    public Mono<AssetFileUploadResponse> uploadAssetFile(Mono<AssetFileUploadRequest> assetFileUploadRequest, ServerWebExchange exchange) {
        return assetFileUploadRequest
                .map(assetFileUploadRequestMapper::toDomain)
                .flatMap(uploadAssetUseCase::upload)
                .map(assetFileUploadResponseMapper::toApiModel);
    }
}
