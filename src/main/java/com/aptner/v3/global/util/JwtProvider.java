package com.aptner.v3.global.util;

import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtProvider {

    public Member claimsToMember(Map<String, Object> claims) {
        Member member = new Member();
        member.setId(((Number) claims.get("id")).longValue());
        member.setUsername((String) claims.get("username"));
        // Add other member fields as needed
        List<MemberRole> roles = Arrays.stream((String[]) claims.get("roles"))
                .map(MemberRole::valueOf)
                .collect(Collectors.toList());
        member.setRoles(roles);
        return member;
    }
}
