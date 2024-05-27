package com.aptner.v3.member.service;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.global.error.ErrorCode;
import com.aptner.v3.global.exception.CustomException;
import com.aptner.v3.global.util.ModelMapperUtil;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.dto.MemberCreateDto;
import com.aptner.v3.member.dto.MemberUpdateDto;
import com.aptner.v3.member.repository.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import software.amazon.awssdk.utils.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

import static com.aptner.v3.CommunityApplication.passwordEncoder;

@Slf4j
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
    public Member signUp(MemberCreateDto.MemberRequest memberRequest) {

        verifySignUp(memberRequest);

        List<MemberRole> memberRoles = memberRequest.getRoles().stream()
                .map(role -> MemberRole.valueOf(role.name()))
                .collect(Collectors.toList());

        memberRequest.setPassword(passwordEncoder().encode( memberRequest.getPassword()));
        memberRequest.setRoles(memberRoles);
        Member mapped = ModelMapperUtil.getModelMapper().map(memberRequest, Member.class);

        return memberRepository.save(mapped);
    }

    private void verifySignUp(MemberCreateDto.MemberRequest memberRequest) {

        String username = memberRequest.getUsername();
        String password = memberRequest.getPassword();
        String passwordConfirm = memberRequest.getPasswordConfirm();

        // check password
        if (!StringUtils.equals(password, passwordConfirm)) {
            throw new CustomException(ErrorCode.PASSWORD_MISMATCH_EXCEPTION);
        }

        // check user
        memberRepository.findByUsername(username)
                .ifPresent(user -> {
                    throw new CustomException(ErrorCode.ALREADY_REGISTERED_USER_EXCEPTION);
                });

    }

    @Transactional
    public Member update(Long memberId, MemberUpdateDto.MemberUpdateRequest memberUpdateRequest) {
        try {
            // nickname
            Member member = memberRepository.getReferenceById(memberId);
            if (!StringUtils.isEmpty(memberUpdateRequest.getNickname())) {
                member.setNickname(memberUpdateRequest.getNickname());
            }

            // image
            if (!StringUtils.isEmpty(memberUpdateRequest.getImage())) {
                member.setImage(member.getImage());
            }

            if (!StringUtils.isEmpty(memberUpdateRequest.getPhone())) {
                member.setPhone(memberUpdateRequest.getPhone());
            }
            return member;
        } catch (EntityNotFoundException e) {
            throw new CustomException(ErrorCode._NOT_FOUND);
        }
    }

}
