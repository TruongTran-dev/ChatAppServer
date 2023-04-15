package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.SubjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepositoy extends JpaRepository<SubjectEntity, Long> {

    Page<SubjectEntity> findAllByNameLikeIgnoreCase(Pageable pageable, String name);
}
