package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.DeviceTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface DeviceTokenRepository extends JpaRepository<DeviceTokenEntity, Long> {

    Optional<DeviceTokenEntity> findFirstByUserId(Long userId);

    Optional<DeviceTokenEntity> findFirstByToken(String token);
}
