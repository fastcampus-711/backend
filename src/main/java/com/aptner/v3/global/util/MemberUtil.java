package com.aptner.v3.global.util;

import com.aptner.v3.auth.dto.CustomUserDetails;
import com.aptner.v3.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class MemberUtil {
    private static Authentication authentication;
    @Autowired
    private static MemberService memberService;

    public static String getUsername() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
        return ((CustomUserDetails)(authentication.getPrincipal())).getUsername();
    }

    public static long getMemberId() {
        authentication = SecurityContextHolder.getContext().getAuthentication();
//        return ((CustomUserDetails)(authentication.getPrincipal())).getId();
        return 1;
    }
}
