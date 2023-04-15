package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.ClassEntity;
import com.kma.project.chatapp.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    Page<StudentEntity> findAllByNameLikeIgnoreCaseAndClassEntity(Pageable pageable, String name, ClassEntity classEntity);

}
