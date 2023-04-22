package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.ClassSubjectMapEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClassSubjectMapRepository extends JpaRepository<ClassSubjectMapEntity, Long> {

    List<ClassSubjectMapEntity> findAllByClassId(Long classId);

}
