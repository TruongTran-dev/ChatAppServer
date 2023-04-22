package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.entity.LearningResultEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LearningResultRepository extends JpaRepository<LearningResultEntity, Long> {

    List<LearningResultEntity> findAllByStudentId(Long studentId);

    LearningResultEntity findByStudentIdAndYear(Long studentId, String year);


}
