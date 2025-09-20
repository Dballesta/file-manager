package org.dballesteros.filemanager.domain.port.client;

import org.dballesteros.filemanager.domain.model.AssetDto;

public interface FilebinClient {
    String uploadFile(AssetDto assetDto);
}
