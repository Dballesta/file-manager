package org.dballesteros.filemanager.infrastructure.h2.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "asset_uploads")
public class AssetUploadEntity {
    @Id
    private Long id;
    private String status;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asset_id", nullable = false)
    private AssetEntity asset;
}
