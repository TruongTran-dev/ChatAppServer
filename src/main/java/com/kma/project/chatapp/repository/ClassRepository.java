package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.ClassEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassRepository extends JpaRepository<ClassEntity, Long> {

    Page<ClassEntity> findAllByNameLikeIgnoreCase(Pageable pageable, String name);

    List<ClassEntity> findAllByIdIn(List<Long> ids);
}
