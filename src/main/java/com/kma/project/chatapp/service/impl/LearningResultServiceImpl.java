package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.LearningResultDetailRequestDto;
import com.kma.project.chatapp.dto.response.cms.LearningResultDetailResponseDto;
import com.kma.project.chatapp.entity.LearningResultDetailEntity;
import com.kma.project.chatapp.entity.LearningResultEntity;
import com.kma.project.chatapp.entity.StudentEntity;
import com.kma.project.chatapp.entity.SubjectEntity;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.LearningResultDetailMapper;
import com.kma.project.chatapp.repository.*;
import com.kma.project.chatapp.service.LearningResultService;
import com.kma.project.chatapp.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class LearningResultServiceImpl implements LearningResultService {

    @Autowired
    LearningResultDetailRepository resultDetailRepository;

    @Autowired
    LearningResultRepository resultRepository;

    @Autowired
    LearningResultDetailMapper mapper;

    @Autowired
    LearningResultDetailRepository learningResultDetailRepository;

    @Autowired
    SubjectRepositoy subjectRepositoy;

    @Autowired
    StudentRepository studentRepository;

    @Autowired
    DeviceTokenRepository deviceTokenRepository;

    @Autowired
    LearningResultRepository learningResultRepository;

    @Autowired
    NotificationService notificationService;

    @Override
    public LearningResultDetailResponseDto updateScore(Long id, LearningResultDetailRequestDto dto) {
        LearningResultDetailEntity resultDetail = resultDetailRepository.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.result-detail-not-found")).build());
        mapper.update(dto, resultDetail);

        if (resultDetail.getM15TestScore() != null && resultDetail.getM45TestScore() != null
                && resultDetail.getOralTestScore() != null && resultDetail.getSemesterTestScore() != null) {
            // điểm đánh giá thường xuyên
            Float regularReviewScore = (resultDetail.getM15TestScore() + resultDetail.getOralTestScore()) / 2;
            // điểm trung bình môn học của 1 học kì
            Float semesterAverageScore = (regularReviewScore + 2 * resultDetail.getM45TestScore() + 3 * resultDetail.getSemesterTestScore()) / 6;
            resultDetail.setSemesterSummaryScore(semesterAverageScore);

            LearningResultEntity learningResultEntity = learningResultRepository.getById(resultDetail.getLearningResultId());
            StudentEntity studentEntity = studentRepository.getById(learningResultEntity.getStudentId());

            if (studentEntity.getParentId() != null) {
                // bắn noti lên app
                String message = "Học sinh :name đã có điểm học kì :semester";
                message = message.replace(":name", studentEntity.getName());
                message = message.replace(":semester", resultDetail.getTerm().toString());

                String finalMessage = message;
                deviceTokenRepository.findFirstByUserId(studentEntity.getParentId()).ifPresent(deviceTokenEntity -> {
                    String deviceToken = deviceTokenEntity.getToken();
                    notificationService.sendNotification(deviceToken, "App Chat", finalMessage);
                });
            }
        }
        LearningResultDetailResponseDto responseDto = mapper.convertToDto(resultDetailRepository.save(resultDetail));
        Optional<SubjectEntity> subjectEntity = subjectRepositoy.findById(resultDetail.getSubjectId());
        subjectEntity.ifPresent(entity -> responseDto.setSubjectName(entity.getName()));
        return responseDto;
    }

    @Override
    public List<LearningResultDetailResponseDto> getAllResult(Long studentId, String year, Integer term) {
        return resultDetailRepository.getAllResultDetail(studentId, year, term);
    }

    @Override
    public void calculateFinalScore(Long studentId, String semesterYear) {
        // semesterYear: 2023-2024
        LearningResultEntity learningResultEntity = resultRepository.findByStudentIdAndYear(studentId, semesterYear);

        // lấy điểm các môn trong học kì 1
        List<LearningResultDetailResponseDto> resultDetailResponse1 = learningResultDetailRepository
                .getAllResultDetail(studentId, semesterYear, 1);
        Float subjectSemesterScore1 = 0f;
        for (LearningResultDetailResponseDto item : resultDetailResponse1) {
            if (item.getSemesterSummaryScore() == null) {
                item.setSemesterSummaryScore(0f);
            }
            subjectSemesterScore1 += item.getSemesterSummaryScore();
        }

        // lấy điểm các môn trong học kì 2
        List<LearningResultDetailResponseDto> resultDetailResponse2 = learningResultDetailRepository
                .getAllResultDetail(studentId, semesterYear, 2);
        Float subjectSemesterScore2 = 0f;
        for (LearningResultDetailResponseDto item : resultDetailResponse2) {
            if (item.getSemesterSummaryScore() == null) {
                item.setSemesterSummaryScore(0f);
            }
            subjectSemesterScore2 += item.getSemesterSummaryScore();
        }

        Float hk1SubjectMediumScore = subjectSemesterScore1 / resultDetailResponse1.size();
        Float hk2SubjectMediumScore = subjectSemesterScore2 / resultDetailResponse2.size();

        Float mediumScore = (hk1SubjectMediumScore + 2 * hk2SubjectMediumScore) / 3;
        learningResultEntity.setHk1SubjectMediumScore(hk1SubjectMediumScore);
        learningResultEntity.setHk2SubjectMediumScore(hk2SubjectMediumScore);
        learningResultEntity.setMediumScore(mediumScore);
        resultRepository.save(learningResultEntity);
    }

}
