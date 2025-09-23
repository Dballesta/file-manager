package org.dballesteros.filemanager.infrastructure.h2;

import org.dballesteros.filemanager.infrastructure.h2.entity.AssetEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface AssetJpaRepository extends JpaRepository<AssetEntity, Long>, JpaSpecificationExecutor<AssetEntity> {
    Optional<AssetEntity> findByUuid(UUID uuid);
}

