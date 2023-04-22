package com.kma.project.chatapp.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "learning_results")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@Builder
public class LearningResultEntity extends BaseEntity {

    @Column(name = "student_id")
    private Long studentId;

    @Column(name = "year")
    private String year; // năm học

    @Column(name = "medium_score")
    private Float mediumScore; // điểm trung bình môn cả năm

    @Column(name = "hk1_subject_medium_score")
    private Float hk1SubjectMediumScore; // điểm trung bình các môn học kì 1

    @Column(name = "hk2_subject_medium_score")
    private Float hk2SubjectMediumScore; // điểm trung bình các môn học kì 2

}
