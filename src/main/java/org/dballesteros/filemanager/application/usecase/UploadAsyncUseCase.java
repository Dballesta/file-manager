package org.dballesteros.filemanager.application.usecase;

import org.dballesteros.filemanager.domain.model.AssetDto;
import org.springframework.scheduling.annotation.Async;

public interface UploadAsyncUseCase {
    @Async("uploadExecutor")
    void uploadFileAsync(AssetDto assetDto);
}
