package com.kma.project.chatapp.repository;

import com.kma.project.chatapp.dto.response.cms.LearningResultDetailResponseDto;
import com.kma.project.chatapp.entity.LearningResultDetailEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LearningResultDetailRepository extends JpaRepository<LearningResultDetailEntity, Long> {

    @Query(value = " select new com.kma.project.chatapp.dto.response.cms.LearningResultDetailResponseDto(ld.id, se.id," +
            " se.name, l.id, ld.oralTestScore, ld.m15TestScore, ld.m45TestScore, ld.semesterTestScore, ld.semesterSummaryScore)" +
            " from StudentEntity s " +
            " join ClassSubjectMapEntity cs on s.classEntity.id = cs.classId " +
            " join SubjectEntity se on cs.subjectId = se.id " +
            " join LearningResultEntity l on s.id = l.studentId " +
//            " join SemesterMapEntity sm on l.id = sm.learningResultId " +
            " join LearningResultDetailEntity ld on l.id = ld.learningResultId" +
            " where s.id = :studentId and l.year = :year and ld.term = :term and se.id = ld.subjectId")
    List<LearningResultDetailResponseDto> getAllResultDetail(@Param("studentId") Long studentId, @Param("year") String year,
                                                             @Param("term") Integer term);

//    List<LearningResultDetailEntity> findAllByLearningResultId(Long learningResultId);
}
