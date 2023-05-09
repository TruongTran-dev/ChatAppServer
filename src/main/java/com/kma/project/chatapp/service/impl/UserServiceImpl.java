package com.kma.project.chatapp.service.impl;

import com.kma.project.chatapp.dto.request.auth.*;
import com.kma.project.chatapp.dto.response.auth.JwtResponse;
import com.kma.project.chatapp.dto.response.auth.PageResponse;
import com.kma.project.chatapp.dto.response.auth.UserOutputDto;
import com.kma.project.chatapp.dto.response.cms.ClassResponseDto;
import com.kma.project.chatapp.dto.response.cms.StudentResponseDto;
import com.kma.project.chatapp.entity.RefreshToken;
import com.kma.project.chatapp.entity.RoleEntity;
import com.kma.project.chatapp.entity.StudentEntity;
import com.kma.project.chatapp.entity.UserEntity;
import com.kma.project.chatapp.enums.ERole;
import com.kma.project.chatapp.exception.AppException;
import com.kma.project.chatapp.exception.AppResponseDto;
import com.kma.project.chatapp.mapper.ClassMapper;
import com.kma.project.chatapp.mapper.StudentMapper;
import com.kma.project.chatapp.mapper.UserMapper;
import com.kma.project.chatapp.repository.ClassRepository;
import com.kma.project.chatapp.repository.RoleRepository;
import com.kma.project.chatapp.repository.StudentRepository;
import com.kma.project.chatapp.repository.UserRepository;
import com.kma.project.chatapp.security.jwt.JwtUtils;
import com.kma.project.chatapp.security.services.UserDetailsImpl;
import com.kma.project.chatapp.service.RefreshTokenService;
import com.kma.project.chatapp.service.UserService;
import com.kma.project.chatapp.utils.PageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RefreshTokenService refreshTokenService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ClassMapper classMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Value("${viet.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Transactional
    @Override
    public AppResponseDto signUp(UserInputDto inputDto) {
        if (userRepository.existsByUsername(inputDto.getUsername())) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.username-exist")).build();
        }

        if (userRepository.existsByEmail(inputDto.getEmail())) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.email-exist")).build();
        }
        // Create new user's account
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(inputDto.getUsername());
        userEntity.setEmail(inputDto.getEmail());
        userEntity.setFullName(inputDto.getFullName());
        userEntity.setPhone(inputDto.getPhone());
        userEntity.setPassword(passwordEncoder.encode(inputDto.getPassword()));
        userEntity.setIsFillProfileKey(false);
        Set<RoleEntity> roles = new HashSet<>();
        RoleEntity userRole;
        if (inputDto.getRoles() != null && !inputDto.getRoles().isEmpty()) {
            for (String item : inputDto.getRoles()) {
                userRole = roleRepository.findByName(ERole.valueOf(item))
                        .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.role-not-exist")).build());
                roles.add(userRole);
            }
        } else {
            userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.role-not-exist")).build());
            roles.add(userRole);
        }
        userEntity.setRoles(roles);
        userRepository.save(userEntity);
        return AppResponseDto.builder().httpStatus(200).message("Đăng kí thành công").build();
    }

    @Transactional
    @Override
    public AppResponseDto signIn(LoginRequest loginRequest) {
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        } catch (Exception e) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.login-fail")).build();
        }

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = "Bearer " + jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userDetails.getUsername());
        Date expiredDate = new Date((new Date()).getTime() + jwtExpirationMs);
        LocalDateTime localDate = expiredDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        JwtResponse jwtResponse = JwtResponse.builder()
                .refreshToken(refreshToken.getToken())
                .id(userDetails.getUserId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .accessToken(jwt)
                .expiredAccessDate(localDate.toString())
                .expiredRefreshDate(refreshToken.getExpiryDate().toString())
                .roles(roles)
                .isFillProfileKey(userDetails.getIsFillProfileKey())
                .build();

        return AppResponseDto.builder().data(jwtResponse).httpStatus(200).message("Đăng nhập thành công").build();
    }

    @Override
    public void verifyOtp(OtpRequestDto otpRequestDto) {
        UserEntity userEntity = userRepository.findByEmail(otpRequestDto.getEmail())
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.email-not-found")).build());

        if (!otpRequestDto.getOtp().equals(userEntity.getOtp())) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.otp-not-valid")).build();
        }
    }

    @Transactional
    @Override
    public void createNewPassword(NewPasswordRequestDto newPasswordRequestDto) {
        UserEntity userEntity = userRepository.findByEmail(newPasswordRequestDto.getEmail())
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.email-not-found")).build());
        if (!newPasswordRequestDto.getPassword().equals(newPasswordRequestDto.getConfirmPassword())) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.password-not-correct")).build();
        }
        userEntity.setPassword(passwordEncoder.encode(newPasswordRequestDto.getPassword()));
        userRepository.save(userEntity);
    }

    @Transactional
    @Override
    public void changePassword(ChangePasswordRequestDto changePasswordRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        if (!changePasswordRequestDto.getPassword().equals(changePasswordRequestDto.getConfirmPassword())) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.password-not-correct")).build();
        }
        if (!passwordEncoder.matches(changePasswordRequestDto.getCurrentPassword(), userDetails.getPassword())) {
            throw AppException.builder().errorCodes(Collections.singletonList("error.password-not-correct")).build();
        }
        UserEntity userEntity = userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.username-not-found")).build());
        userEntity.setPassword(passwordEncoder.encode(changePasswordRequestDto.getPassword()));
        userRepository.save(userEntity);
    }

    @Override
    public PageResponse<UserOutputDto> getAllUser(Integer page, Integer size, String sort, String search) {
        Pageable pageable = PageUtils.customPageable(page, size, sort);
        search = PageUtils.buildSearch(search);
        Page<UserEntity> pageUser = userRepository.findAllByEmailLikeIgnoreCaseOrUsernameLikeIgnoreCase(pageable, search, search);
        return PageUtils.formatPageResponse(pageUser.map(userEntity -> {
            return userMapper.convertToDto(userEntity);
        }));
    }

    @Transactional
    @Override
    public UserOutputDto updateUser(Long userId, UserUpdateDto dto) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.entity-not-found")).build());
        userMapper.update(dto, userEntity);
        if (!userEntity.getUsername().equals(dto.getUsername()) && dto.getUsername() != null) {
            if (userRepository.existsByUsername(dto.getUsername())) {
                throw AppException.builder().errorCodes(Collections.singletonList("error.username-exist")).build();
            }
            userEntity.setUsername(dto.getUsername());
        }

        if (!userEntity.getEmail().equals(dto.getEmail()) && dto.getEmail() != null) {
            if (userRepository.existsByEmail(dto.getEmail())) {
                throw AppException.builder().errorCodes(Collections.singletonList("error.email-exist")).build();
            }
            userEntity.setEmail(dto.getEmail());
        }
        if (dto.getPassword() != null && dto.getConfirmPassword() != null) {
            if (!dto.getPassword().equals(dto.getConfirmPassword())) {
                throw AppException.builder().errorCodes(Collections.singletonList("error.password-not-correct")).build();
            }
            userEntity.setPassword(passwordEncoder.encode(dto.getPassword()));
        }

        Set<RoleEntity> roles = new HashSet<>();
        if (dto.getRoles() != null && !dto.getRoles().isEmpty()) {
            for (String item : dto.getRoles()) {
                RoleEntity userRole = roleRepository.findByName(ERole.valueOf(item))
                        .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.role-not-exist")).build());
                roles.add(userRole);
            }
            userEntity.setRoles(roles);
        }
        UserOutputDto outputDto = userMapper.convertToDto(userEntity);
        mapStudentResponse(outputDto, userEntity);
        return outputDto;
    }

    @Transactional
    @Override
    public void delete(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.entity-not-found")).build());
        userRepository.delete(userEntity);
    }

    @Override
    public UserOutputDto getDetailUser(Long userId) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> AppException.builder().errorCodes(Collections.singletonList("error.entity-not-found")).build());
        UserOutputDto outputDto = userMapper.convertToDto(userEntity);
        mapStudentResponse(outputDto, userEntity);
        return outputDto;
    }

    public void mapStudentResponse(UserOutputDto outputDto, UserEntity userEntity) {
        if (userEntity.getStudentIds() != null) {
            Set<Long> studentIds = new HashSet<>();
            for (String item : userEntity.getStudentIds()) {
                studentIds.add(Long.valueOf(item));
            }
            List<StudentEntity> studentEntities = studentRepository.findAllByIdIn(studentIds);
            List<StudentResponseDto> studentOutputs = new ArrayList<>();
            for (StudentEntity studentEntity : studentEntities) {
                StudentResponseDto studentResponseDto = studentMapper.convertToDto(studentEntity);
                if (studentEntity.getClassEntity() != null) {
                    ClassResponseDto classResponseDto = classMapper.convertToDto(studentEntity.getClassEntity());
                    studentResponseDto.setClassResponse(classResponseDto);
                    studentResponseDto.setClassName(classResponseDto.getName());
                }
                studentOutputs.add(studentResponseDto);
            }
            outputDto.setStudents(studentOutputs);
        }
    }

}
