package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.dto.response.cms.StudentLearningResultResponseDto;
import com.kma.project.chatapp.entity.ClassEntity;
import com.kma.project.chatapp.entity.StudentEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<StudentEntity, Long> {

    Page<StudentEntity> findAllByNameLikeIgnoreCaseAndClassEntity(Pageable pageable, String name, ClassEntity classEntity);

    @Query(value = " select new com.kma.project.chatapp.dto.response.cms.StudentLearningResultResponseDto(s.id, s.name, s.code, " +
            " s.dateOfBirth, s.imageUrl, c.name, l.mediumScore, l.hk1SubjectMediumScore, l.hk2SubjectMediumScore)" +
            " from StudentEntity s " +
            " join LearningResultEntity l on s.id = l.studentId " +
            " join ClassEntity c on s.classEntity.id = c.id" +
            " where (:year is null or l.year = :year) and (:classId is null or c.id = :classId) ")
    Page<StudentLearningResultResponseDto> findAllStudent(Pageable pageable, String year, Long classId);

    Optional<StudentEntity> findByCode(String code);

    List<StudentEntity> findAllByIdIn(Collection<Long> studentIds);
}

