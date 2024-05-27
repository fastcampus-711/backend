package com.aptner.v3.auth.dto;

import com.aptner.v3.member.Member;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * The type Custom user details dto.
 */
@ToString
public class CustomUserDetails implements UserDetails {

    private Member member;

    public CustomUserDetails(Member member) {
        this.member = member;
    }

    /**
     * Role 반환
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return member.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.name()))
                .collect(Collectors.toList());
    }

    /**
     * Gets member.
     *
     * @return the member
     */
    public final Member getMember() {
        return member;
    }

    public long getId() {
        return member.getId();
    }

    /**
     * Password 반환
     */
    @Override
    public String getPassword() {
        return member.getPassword();
    }

    /**
     * Username 반환
     */
    @Override
    public String getUsername() {
        return member.getUsername();
    }

    /**
     * Username 반환
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 계정 잠김 여부 반환
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 비밀 번호 만료 여부
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 계정 사용 가능 여부
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
