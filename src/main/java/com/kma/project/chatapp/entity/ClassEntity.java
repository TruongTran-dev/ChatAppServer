package com.kma.project.chatapp.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "classes")
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class ClassEntity extends BaseEntity {

    private String code;

    private String name;

    private String year;

    @OneToMany(mappedBy = "classEntity")
    private List<StudentEntity> studentList;

}
