package org.dballesteros.filemanager.infrastructure.client;

import lombok.RequiredArgsConstructor;
import openapi.client.api.FileApi;
import org.dballesteros.filemanager.domain.model.AssetDto;
import org.dballesteros.filemanager.domain.model.exception.ExceptionDetail;
import org.dballesteros.filemanager.domain.port.client.UploadClientPort;
import org.dballesteros.filemanager.infrastructure.config.FilebinConfigProps;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.Arrays;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class FilebinUploadAdapter implements UploadClientPort {

    private final FileApi fileApi;
    private final FilebinConfigProps filebinConfigProps;

    @Override
    public Mono<Void> uploadFile(final AssetDto assetDto) {
        return this.fileApi.binFilenamePost(
                        this.filebinConfigProps.getBinId(),
                        assetDto.getFilename(),
                        this.filebinConfigProps.getCid(),
                        Base64.getEncoder().encodeToString(Arrays.copyOf(assetDto.getEncodedFile(), assetDto.getEncodedFile().length)))
                .retryWhen(Retry.backoff(3, Duration.ofMillis(500)))
                .onErrorResume(throwable ->
                        Mono.error(new ExceptionDetail("ERROR_UPLOADING", "Error Uploading to filebin", throwable)));
    }
}
