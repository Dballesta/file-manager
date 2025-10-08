package org.dballesteros.filemanager.domain.service;

import org.dballesteros.filemanager.domain.model.AssetDomain;

public interface AsyncUploadService {

    void uploadFileAsync(AssetDomain assetDomain);
}
