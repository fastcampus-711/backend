package com.aptner.v3.user.domain;

import com.aptner.v3.user.type.Role;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "password"),
        @Index(columnList = "roles"),
})
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false)
    private String username;

    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false)
    String password;

    @NotNull(message = "필수 입력값입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    /*@NotNull(message = "필수 입력값입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @OneToMany(targetEntity = Role.class, fetch = FetchType.LAZY)
    private List<Role> roles;

    public List<Role> getRoleList() {
        return roles;
    }*/

}
