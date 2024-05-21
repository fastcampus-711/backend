package com.aptner.v3.member.service;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.custom.CustomException;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.dto.MemberDto;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.aptner.v3.CommunityApplication.passwordEncoder;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDetails user = memberRepository.findByUsername(username)
                .map(CustomUserDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return user;
    }

    @Transactional
    public void signUp(MemberDto.MemberRequest memberRequest) {

        String username = memberRequest.getUsername();
        String password = memberRequest.getPassword();
        verifySignUp(memberRequest);

        List<MemberRole> memberRoles = memberRequest.getRoles().stream()
                .map(role -> MemberRole.valueOf("ROLE_" + role.name()))
                .collect(Collectors.toList());

        Member memberEntity = Member.builder()
                .username(username)
                .password(passwordEncoder().encode(password))
                .roles(memberRoles)
                .build();

        memberRepository.save(memberEntity);
    }

    private void verifySignUp(MemberDto.MemberRequest memberRequest) {

        String username = memberRequest.getUsername();
        String password = memberRequest.getPassword();
        String passwordConfirm = memberRequest.getPasswordConfirm();

        // check password
        if (StringUtils.equals(password, passwordConfirm)) {
            // password.equals : BCryptPasswordEncoder (Encoded password does not look like BCrypt password.equals)
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION);
        }

        // check user
        memberRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
                });

    }

}
