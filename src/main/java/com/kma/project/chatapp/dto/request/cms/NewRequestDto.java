package com.kma.project.chatapp.dto.request.cms;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NewRequestDto {

    private String title;

    private String content;

    private String mediaUrl;

    private Integer typeMedia; // 1: image, 2: video
}
