package com.aptner.v3.member;

import com.aptner.v3.maintenance_bill.domain.House;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill.ResidentInfo;
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
@Table(indexes = {
        @Index(name = "resident_info_idx", columnList = "apartment_code, apartment_square_meter, dong, ho")
})
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

    @Embedded
    private ResidentInfo residentInfo;

    public Member(String username, String password, String nickname, String image, String phone, List<MemberRole> roles, House house) {
        this.username = username;
        this.password = password;
        this.nickname = nickname;
        this.image = image;
        this.phone = phone;
        this.roles = roles;
        this.residentInfo = new ResidentInfo(house);
    }

    public static Member of(String username, String password, String nickname, String image, String phone, List<MemberRole> roles, House house) {
        return new Member(username, password, nickname, image, phone, roles, house);
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
