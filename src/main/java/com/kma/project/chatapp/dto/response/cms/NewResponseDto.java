package com.kma.project.chatapp.dto.response.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewResponseDto {

    private Long id;

    private String title;

    private String content;

    private String mediaUrl;

    private Integer typeMedia; // 1: image, 2: video

    private Long createdId;

    private String createdName;

    private String createdFile;

    private LocalDateTime createdAt;
}
