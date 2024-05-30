package com.aptner.v3.member;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
@Entity(name = "users")
public class Member {

    @Id
    @Setter
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Setter
    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false, unique = true, length = 50, updatable = false)
    private String username;

    @Setter
    @Column(length = 50)
    private String nickname;

    @Setter
    @NotNull(message = "필수 입력값입니다.")
    @Column(nullable = false)
    private String password;

    @Setter
    private String image;

    @Setter
    @Column(length = 50)
    private String phone;

    @Setter
    @ElementCollection(targetClass = MemberRole.class, fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    @CollectionTable(name = "user_roles", joinColumns = { @JoinColumn(name = "user_id") })
    @Column(name = "user_role")
    private List<MemberRole> roles;

    public Member(String username, String password, String nickname, String image, String phone, List<MemberRole> roles) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.phone = phone;
        this.roles = roles;
    }

    public static Member of(String username, String password, String nickname, String image, String phone, List<MemberRole> roles) {
        return new Member(username, password, nickname, image, phone, roles);
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
