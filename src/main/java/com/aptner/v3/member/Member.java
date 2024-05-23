package com.aptner.v3.member;

import com.aptner.v3.global.domain.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity(name = "users")
public class Member extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(length = 50)
    private String name;

    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false)
    private String password;

    @Setter
    private String image;

    @Column(length = 50)
    private String phone;

    @ElementCollection(targetClass = MemberRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = { @JoinColumn(name = "USER_ID") })
    @Column(name = "user_role")
    private List<MemberRole> roles;

    public Member(String username, String password, List<MemberRole> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public static Member of(String username, String password, List<MemberRole> roles) {
        return new Member(username, password, roles);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Member member = (Member) o;
        return Objects.equals(id, member.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
