package com.component.repository;

import com.component.model.ComponentEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ComponentRepository extends JpaRepository<ComponentEntity, Long> {
    List<ComponentEntity> findByOwnerUserId(Long ownerUserId);

    @Lock(jakarta.persistence.LockModeType.PESSIMISTIC_READ)
    Optional<ComponentEntity> findByOwnerUserIdAndId(Long ownerUserId, Long componentId);
}
