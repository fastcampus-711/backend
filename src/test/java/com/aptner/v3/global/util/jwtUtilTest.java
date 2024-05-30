package com.aptner.v3.global.util;

import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = JwtUtil.class)
public class jwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;

    @Test
    void parseClaimToMember() {

        Member member = createMember_UserAndAdmin();
        // When
        Map<String, Object> claims = jwtUtil.memberToClaims(member);
        assertEquals(1L, claims.get("id"));

        Member parsed = jwtUtil.claimsToMember(claims);
        assertEquals(1L, parsed.getId());
    }

    @Test
    void getMemberFromClaim() {
        Member member = createMember_User();
        // When
        Map<String, Object> claims = jwtUtil.memberToClaims(member);

        Member parsed = jwtUtil.claimsToMember(claims);
        assertEquals(1L, parsed.getId());

    }

    private Member createMember_UserAndAdmin() {
        // Given
        Member member = new Member();
        member.setId(1L);
        member.setUsername("user");
        member.setRoles(List.of(MemberRole.ROLE_USER, MemberRole.ROLE_ADMIN));

        return member;
    }

    private Member createMember_User() {
        // Given
        Member member = new Member();
        member.setId(1L);
        member.setUsername("user");
        member.setRoles(List.of(MemberRole.ROLE_USER));

        return member;
    }
}
