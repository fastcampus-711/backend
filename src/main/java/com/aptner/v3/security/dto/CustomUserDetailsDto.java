package com.aptner.v3.security.dto;

import com.aptner.v3.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@RequiredArgsConstructor
public class CustomUserDetailsDto implements UserDetails {

    private final User user;

    //ROLE 을 반환하는 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return user.getRoles().toString();
            }
        });

        return collection;
    }

    public long getId() {
        return user.getId();
    }
    //Password 반환하는 메소드
    @Override
    public String getPassword() {
        return user.getPassword();
    }
    //Username 반환하는 메소드
    @Override
    public String getUsername() {
        return user.getUsername();
    }

    //계정 만료 여부 반환하는 메소드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //계정 잠김 여부 반환하는 메소드
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    //비밀번호 만료 여부 반환하는 메소드
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    //계정 사용 가능 여부 반환하는 메소드
    @Override
    public boolean isEnabled() {
        return true;
    }
}
