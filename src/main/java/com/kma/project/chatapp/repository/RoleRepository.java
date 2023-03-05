package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.RoleEntity;
import com.kma.project.chatapp.enums.ERole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {

    Optional<RoleEntity> findByName(ERole name);
}
