package com.aptner.v3.global.util;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.maintenance_bill.domain.House;
import com.aptner.v3.maintenance_bill.domain.type.HouseType;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

import static com.aptner.v3.CommunityApplication.passwordEncoder;

public class MemberUtil {
    private static Authentication authentication;

    public static String getUsername() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails)(authentication.getPrincipal())).getUsername();
    }

    public static Member getMember() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getMember();
        }
        // @test
        Member member = Member.of("user", passwordEncoder().encode("p@ssword"), "nickname1", "https://avatars.githubusercontent.com/u/79270228?v=4", "01011112222", List.of(MemberRole.ROLE_USER), new House(77777, "패캠세븐아파트", HouseType.APARTMENT,115.7,  "701", "103"));

        member.setId(1L);
        return member;
    }
}
