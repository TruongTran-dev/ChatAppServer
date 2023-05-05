package com.kma.project.chatapp.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@TypeDefs(

        {
                @TypeDef(
                        name = "string-array",
                        typeClass = StringArrayType.class
                )
        }
)
public class UserEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String email;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "user_role_map",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles;

    private String otp;

    @Column(name = "full_name")
    private String fullName;

    private String phone;

    @Column(name = "is_fill_profile_key")
    private Boolean isFillProfileKey;

    @Column(name = "file_url")
    private String fileUrl;

    @Column(name = "student_ids", columnDefinition = "varchar[]")
    @Type(type = "string-array")
    private String[] studentIds;

    public UserEntity(String username, String email, String encode) {
        this.username = username;
        this.email = email;
        this.password = encode;
    }
}
