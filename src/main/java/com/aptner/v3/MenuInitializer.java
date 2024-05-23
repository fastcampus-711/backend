package com.aptner.v3;

import com.aptner.v3.category.BoardGroup;
import com.aptner.v3.category.CategoryService;
import com.aptner.v3.category.dto.CategoryDtoRequest;
import com.aptner.v3.member.repository.MemberRepository;
import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.MenuCode;
import com.aptner.v3.menu.MenuService;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuInitializer implements CommandLineRunner {

    private final MenuService menuService;
    private final CategoryService categoryService;
    private final MemberRepository memberRepository;

    @Override
    public void run(String... args) throws Exception {

//        memberRepository.save(Member.of("user", passwordEncoder().encode("p@ssword"), List.of(MemberRole.USER)));
//        memberRepository.save(Member.of("admin", passwordEncoder().encode("p@ssword"), List.of(MemberRole.USER, MemberRole.ADMIN)));

        // intro
        Menu intro = menuService.createMenu(MenuDtoRequest.of(MenuCode.TOP_INFO.name(), MenuCode.TOP_INFO.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.INTRO.name(), MenuCode.INTRO.getKo(), intro.getId()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.APT.name(), MenuCode.APT.getKo(), intro.getId()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.CONTACT.name(), MenuCode.CONTACT.getKo(), intro.getId()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.COMMUNITY.name(), MenuCode.COMMUNITY.getKo(), intro.getId()));

        // notice
        Menu notice = menuService.createMenu(MenuDtoRequest.of(MenuCode.TOP_NOTICE.name(), MenuCode.TOP_NOTICE.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(notice.getId(), MenuCode.NOTICE.name(), MenuCode.NOTICE.getKo(), BoardGroup.NOTICES));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SCHEDULE.name(), MenuCode.SCHEDULE.getKo(), notice.getId()));

        // mandatory
        menuService.createMenu(MenuDtoRequest.of(MenuCode.TOP_MANDATORY.name(), MenuCode.TOP_MANDATORY.getKo(), null));

        // community
        Menu community = menuService.createMenu(MenuDtoRequest.of(MenuCode.TOP_COMMUNITY.name(), MenuCode.TOP_COMMUNITY.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(community.getId(), MenuCode.FREE.name(), MenuCode.FREE.getKo(), BoardGroup.FREES));
        menuService.createMenu(MenuDtoRequest.of(community.getId(), MenuCode.MARKET.name(), MenuCode.MARKET.getKo(), BoardGroup.MARKETS));
        menuService.createMenu(MenuDtoRequest.of(community.getId(), MenuCode.QNA.name(), MenuCode.QNA.getKo(), BoardGroup.QNAS));

        // complaint
        Menu complaint = menuService.createMenu(MenuDtoRequest.of(MenuCode.TOP_COMPLAINT.name(), MenuCode.TOP_COMPLAINT.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(complaint.getId(), MenuCode.COMPLAINT.name(), MenuCode.COMPLAINT.getKo(), BoardGroup.COMPLAINT));    //** 게시판..형태가 필터넹...
        menuService.createMenu(MenuDtoRequest.of(complaint.getId(), MenuCode.MYCOMPLAINT.name(), MenuCode.MYCOMPLAINT.getKo(), BoardGroup.MYCOMPLAINT));

        // fee
        Menu fee = menuService.createMenu(MenuDtoRequest.of(MenuCode.TOP_FEE.name(), MenuCode.TOP_FEE.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.TOTALFEE.name(), MenuCode.TOTALFEE.getKo(), fee.getId()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.MYFEE.name(), MenuCode.MYFEE.getKo(), fee.getId()));

        // 공지사항 분류
        categoryService.createCategory(CategoryDtoRequest.of("엘리베이터", "1", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("공동생활", "2", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("주차장", "3", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("공동현관/복도", "4", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("보안/경비", "5", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("조명", "6", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("조경", "7", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("커뮤니티시설", "8", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("시공사하자", "9", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("도로/인도", "10", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDtoRequest.of("기타", "11", BoardGroup.NOTICES));

        // 자유 게시판 분류
        categoryService.createCategory(CategoryDtoRequest.of("생활/편의", "13", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("음식/카페", "14", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("병원/약국", "15", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("수리/시공", "16", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("투자/부동산", "17", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("교육/육아", "18", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("아파트/동네소식", "19", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("여행", "20", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("살림정보", "21", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("모임/동호회", "22", BoardGroup.FREES));
        categoryService.createCategory(CategoryDtoRequest.of("기타", "23", BoardGroup.FREES));
    }
}
