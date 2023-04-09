package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByUsername(String username);

    Optional<UserEntity> findByEmail(String email);


    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Page<UserEntity> findAllByEmailLikeIgnoreCaseOrUsernameLikeIgnoreCase(Pageable pageable, String email, String username);

}
