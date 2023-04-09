package com.kma.project.chatapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class NewEntity extends BaseEntity {

    private String title;

    @Column(length = 2000)
    private String content;

    @Column(name = "media_url")
    private String mediaUrl;

    @Column(name = "type_media")
    private Integer typeMedia; // 1: image, 2: video

}
