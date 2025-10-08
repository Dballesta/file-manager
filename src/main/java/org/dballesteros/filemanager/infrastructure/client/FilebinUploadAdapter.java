package org.dballesteros.filemanager.infrastructure.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import openapi.client.api.FileApi;
import org.dballesteros.filemanager.domain.model.AssetDomain;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.dballesteros.filemanager.domain.port.client.UploadClientPort;
import org.dballesteros.filemanager.infrastructure.config.FilebinConfigProps;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import reactor.core.Exceptions;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class FilebinUploadAdapter implements UploadClientPort {

    private final FileApi fileApi;
    private final FilebinConfigProps filebinConfigProps;

    private static Mono<AssetDomain> getAssetDtoMonoWithUpdateData(final AssetDomain assetDomain, final String baseUrl, final String binId) {
        assetDomain.setUploadDateEnd(Instant.now());
        assetDomain.setUrl(String.join("/", baseUrl, binId, assetDomain.getFilename()));
        return Mono.just(assetDomain);
    }
    
    @Override
    public Mono<AssetDomain> uploadFile(final AssetDomain assetDomain) {

        return this.fileApi.binFilenamePost(
                        this.filebinConfigProps.getBinId(),
                        assetDomain.getFilename(),
                        this.filebinConfigProps.getCid(),
                        new ByteArrayResource(assetDomain.getEncodedFile()))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(500)))
                .then(FilebinUploadAdapter.getAssetDtoMonoWithUpdateData(assetDomain, this.filebinConfigProps.getBaseUrl(), this.filebinConfigProps.getBinId()))
                .onErrorMap(throwable -> {
                    final Throwable unwrapped = Exceptions.unwrap(throwable);
                    final Throwable root = NestedExceptionUtils.getMostSpecificCause(unwrapped);
                    return new ExceptionDetail("ERROR_UPLOADING", "Error Uploading to filebin", root);
                })
                .doOnError(throwable -> log.error("Error:", throwable));
    }
}
