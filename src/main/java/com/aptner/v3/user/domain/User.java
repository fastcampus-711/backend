package com.aptner.v3.user.domain;

import com.aptner.v3.user.type.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity(name = "users")
@EntityListeners(AuditingEntityListener.class)
@Table(
        indexes = {
        @Index(columnList = "username"),
        @Index(columnList = "password"),
        @Index(columnList = "roles"),
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false,unique = true, length = 50)
    private String username;
    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false, length = 50)
    private String name;
    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false)
    private String password;

    @Setter
    private String image;

    @NotNull(message = "필수 입력값입니다.")
    @Column(length = 50)
    private String phone;

    @NotNull(message = "필수 입력값입니다.")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole roles;

    public User(String username, String name, String password) {
        this.username = username;
        this.name = name;
        this.password = password;
    }

    public static User of(String username, String name, String password) {
        return new User(username, name, password);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
