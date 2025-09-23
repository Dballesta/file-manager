package org.dballesteros.filemanager.domain.model;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@AllArgsConstructor
@Builder
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
}
