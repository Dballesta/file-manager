package org.dballesteros.filemanager.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AssetDto {
    private String id;
    private String filename;
    private byte[] encodedFile;
    private String contentType;
    private String url;
    private Integer size;
    private Instant uploadDateStart;
    private Instant uploadDateEnd;

    public AssetDto(String id, String filename, String contentType, String url, Integer size, Instant uploadDateStart, Instant uploadDateEnd) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
        this.size = size;
        this.uploadDateStart = uploadDateStart;
        this.uploadDateEnd = uploadDateEnd;
    }

    public AssetDto(String id, String filename, String contentType, String url, Integer size, Instant uploadDateStart) {
        this.id = id;
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
        this.size = size;
        this.uploadDateStart = uploadDateStart;
    }

    public AssetDto(Instant uploadDateStart, Instant uploadDateEnd, String filename, String contentType) {
        this.uploadDateStart = uploadDateStart;
        this.uploadDateEnd = uploadDateEnd;
        this.filename = filename;
        this.contentType = contentType;
    }
}
