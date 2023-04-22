package com.kma.project.chatapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "learning_result_details")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class LearningResultDetailEntity extends BaseEntity {

    @Column(name = "term")
    private Integer term;

    @Column(name = "learning_result_id")
    private Long learningResultId;

    @Column(name = "subject_id")
    private Long subjectId;

    @Column(name = "oral_test_score")
    private Float oralTestScore; // điểm kiểm tra miệng

    @Column(name = "m15_test_score")
    private Float m15TestScore; // điểm kiểm tra 15 phut

    @Column(name = "m45_test_score")
    private Float m45TestScore; // điểm kiểm tra 45 phut

    @Column(name = "semester_test_score")
    private Float semesterTestScore; // điểm kiểm tra học kì

    @Column(name = "semester_summary_score")
    private Float semesterSummaryScore; // điểm trung bình môn của 1 học kì


//    private Float yearSummaryScore; // điểm tổng kết cả năm

}
