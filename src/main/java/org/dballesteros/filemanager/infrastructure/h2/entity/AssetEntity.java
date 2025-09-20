package org.dballesteros.filemanager.infrastructure.h2.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor

@Entity
@Table(name = "assets")
public class AssetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, updatable = false)
    private UUID uuid;

    private String filename;
    private String contentType;
    private String url;
    private Integer size;
    private Instant uploadDateStart;
    private Instant uploadDateEnd;

    public AssetEntity(UUID uuid, String filename, String contentType, String url, Integer size, Instant uploadDateStart, Instant uploadDateEnd) {
        this.uuid = uuid;
        this.filename = filename;
        this.contentType = contentType;
        this.url = url;
        this.size = size;
        this.uploadDateStart = uploadDateStart;
        this.uploadDateEnd = uploadDateEnd;
    }

    @PrePersist
    public void generateUuid() {
        if (this.uuid == null) {
            this.uuid = UUID.randomUUID();
        }
    }
}
