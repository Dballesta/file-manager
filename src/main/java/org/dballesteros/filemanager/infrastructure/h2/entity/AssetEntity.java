package org.dballesteros.filemanager.infrastructure.h2.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "assets")
public class AssetEntity {
    @Id
    private Long id;
    private String idClient;
    private String filename;
    private String contentType;
    private String url;
    private Integer size;
    private String uploadDateStart;
    private String uploadDateEnd;

    public AssetEntity(String idClient, String filename, String contentType, String url, Integer size, String uploadDateStart, String uploadDateEnd) {
        this.idClient = idClient;
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
        this.size = size;
        this.uploadDateStart = uploadDateStart;
        this.uploadDateEnd = uploadDateEnd;
    }
}
