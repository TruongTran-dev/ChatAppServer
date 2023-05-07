package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.cms.StudentRequestDto;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.cms.StudentLearningResultResponseDto;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;
import com.kma.project.chatapp.entity.*;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.mapper.ClassMapper;
import com.kma.project.chatapp.mapper.StudentMapper;
import com.kma.project.chatapp.repository.*;
import com.kma.project.chatapp.service.StudentService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Transactional(readOnly = true)
@Service
public class StudentServiceImpl implements StudentService {

    @Autowired
    private StudentRepository repositoy;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private LearningResultRepository learningResultRepository;

    @Autowired
    private LearningResultDetailRepository learningResultDetailRepository;

    @Autowired
    private SubjectRepositoy subjectRepositoy;

    @Autowired
    private ClassSubjectMapRepository classSubjectMapRepository;

    @Autowired
    private StudentMapper mapper;

    @Autowired
    private ClassMapper classMapper;

    @Transactional
    @Override
    public StudentResponseDto add(StudentRequestDto dto) {
        ClassEntity classEntity = classRepository.findById(dto.getClassId())
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
        StudentEntity entity = mapper.convertToEntity(dto);
        entity.setClassEntity(classEntity);
        repositoy.save(entity);
        entity.setCode("SSID" + "00" + entity.getId());
        repositoy.save(entity);
        // thêm mới kết quả học tập
        LearningResultEntity learningResultEntity = LearningResultEntity.builder()
                .studentId(entity.getId())
                .year(dto.getSemesterYear())
                .build();
        learningResultRepository.save(learningResultEntity);


        // thêm mới chi tiết điểm từng môn học
        List<LearningResultDetailEntity> learningResultDetailEntityList = new ArrayList<>();

        // lấy các môn học trong lớp
        List<ClassSubjectMapEntity> classSubjectMapEntities = classSubjectMapRepository.findAllByClassId(dto.getClassId());
        List<Long> subjectIds = new ArrayList<>();
        classSubjectMapEntities.forEach(classSubjectMapEntity -> {
            subjectIds.add(classSubjectMapEntity.getSubjectId());
        });
        List<SubjectEntity> subjectEntityList = subjectRepositoy.findAllById(subjectIds);

        // set từng môn
        // 1 năm có 2 kì
        for (int i = 1; i <= 2; i++) {
            int finalI = i;
            subjectEntityList.forEach(subjectEntity -> {
                LearningResultDetailEntity learningResultDetailEntity = new LearningResultDetailEntity();
                learningResultDetailEntity.setSubjectId(subjectEntity.getId());
                learningResultDetailEntity.setTerm(finalI);
                learningResultDetailEntity.setLearningResultId(learningResultEntity.getId());
                learningResultDetailEntityList.add(learningResultDetailEntity);
            });
        }
        learningResultDetailRepository.saveAll(learningResultDetailEntityList);

        StudentResponseDto responseDto = mapper.convertToDto(entity);
        responseDto.setClassName(classEntity.getName());
        return responseDto;
    }

    @Transactional
    @Override
    public StudentResponseDto update(Long id, StudentRequestDto dto) {
        StudentEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.student-not-found")).build());
        if (dto.getClassId() != null && !Objects.equals(entity.getClassEntity().getId(), dto.getClassId())) {
            ClassEntity classEntity = classRepository.findById(dto.getClassId())
                    .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.class-not-found")).build());
            entity.setClassEntity(classEntity);
        }
        mapper.update(dto, entity);
        StudentResponseDto responseDto = mapper.convertToDto(entity);
        responseDto.setClassName(entity.getClassEntity().getName());
        return responseDto;
    }

    @Override
    public StudentResponseDto getDetail(Long id, String semesterYear) {
        StudentEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.student-not-found")).build());
        StudentResponseDto studentResponseDto = mapper.convertToDto(entity);
        studentResponseDto.setClassResponse(classMapper.convertToDto(entity.getClassEntity()));
        studentResponseDto.setClassName(entity.getClassEntity().getName());

        if (semesterYear != null) {
            LearningResultEntity learningResultEntity = learningResultRepository.findByStudentIdAndYear(id, semesterYear);
            if (learningResultEntity != null) {
                studentResponseDto.setMediumScore(learningResultEntity.getMediumScore());
                studentResponseDto.setHk1SubjectMediumScore(learningResultEntity.getHk1SubjectMediumScore());
                studentResponseDto.setHk2SubjectMediumScore(learningResultEntity.getHk2SubjectMediumScore());
            }
        }

        return studentResponseDto;
    }

    @Override
    public StudentResponseDto getDetailByCode(String code) {
        StudentEntity entity = repositoy.findByCode(code)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.student-not-found")).build());

        StudentResponseDto studentResponseDto = mapper.convertToDto(entity);
        studentResponseDto.setClassResponse(classMapper.convertToDto(entity.getClassEntity()));
        studentResponseDto.setClassName(entity.getClassEntity().getName());
        return studentResponseDto;
    }

    @Transactional
    @Override
    public void delete(Long id) {
        StudentEntity entity = repositoy.findById(id)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.student-not-found")).build());

        List<LearningResultEntity> learningResultList = learningResultRepository.findAllByStudentId(id);

        List<LearningResultDetailEntity> learningResultDetailEntityList = new ArrayList<>();
//        learningResultList.forEach(learningResultEntity -> {
//            learningResultDetailEntityList.addAll(learningResultDetailRepository
//                    .findAllByLearningResultId(learningResultEntity.getId()));
//        });

        // xóa chi tiết điểm
        learningResultDetailRepository.deleteAll(learningResultDetailEntityList);
        learningResultRepository.deleteAll(learningResultList);

        repositoy.delete(entity);
    }

    @Transactional
    @Override
    public PageResponse<StudentLearningResultResponseDto> getAllStudent(Integer page, Integer size, String sort,
                                                                        String search, String year, Long classId) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);

//        Page<StudentEntity> pageEntity = repositoy.
//                findAllByNameLikeIgnoreCaseAndClassEntity(pageable, PageUtils.buildSearch(search), classEntity);
//
//        return PageUtils.formatPageResponse(pageEntity.map(entity -> {
//            StudentResponseDto responseDto = mapper.convertToDto(entity);
//            responseDto.setClassResponse(classMapper.convertToDto(classEntity));
//            return responseDto;
//        }));
        return PageUtils.formatPageResponse(repositoy.findAllStudent(pageable, year, classId));
    }
}
