package com.aptner.v3;

import com.aptner.v3.board.category.CategoryService;
import com.aptner.v3.menu.MenuCode;
import com.aptner.v3.menu.MenuService;
import com.aptner.v3.menu.dto.MenuDtoRequest;
import com.aptner.v3.menu.dto.MenuDtoResponse;
import com.aptner.v3.security.repository.UserDetailsRepository;
import com.aptner.v3.user.domain.UserEntity;
import com.aptner.v3.user.type.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuInitializer implements CommandLineRunner {

    private final MenuService menuService;
    private final CategoryService categoryService;
    private final UserDetailsRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        userRepository.save(UserEntity.of("user", "tempPassword12!@", Role.USER));
        userRepository.save(UserEntity.of("admin", "tempPassword12!@", Role.ADMIN));

        // intro
        MenuDtoResponse intro = menuService.createMenu(MenuDtoRequest.of(MenuCode.INFO.name(), MenuCode.INFO.getKo(), null));
        System.out.println(intro);
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_INTRO.name(),MenuCode.SUB_INTRO.getKo(), intro.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_APT.name(),MenuCode.SUB_APT.getKo(), intro.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_CONTACT.name(),MenuCode.SUB_CONTACT.getKo(), intro.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_COMMUNITY.name(),MenuCode.SUB_COMMUNITY.getKo(), intro.id()));

        // notice
        MenuDtoResponse notice = menuService.createMenu(MenuDtoRequest.of(MenuCode.NOTICE.name(), MenuCode.NOTICE.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_NOTICE.name(),MenuCode.SUB_NOTICE.getKo(), notice.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_SCHEDULE.name(),MenuCode.SUB_SCHEDULE.getKo(), notice.id()));

        // mandatory
        menuService.createMenu(MenuDtoRequest.of(MenuCode.MANDATORY.name(), MenuCode.MANDATORY.getKo(), null));

        // community
        MenuDtoResponse community = menuService.createMenu(MenuDtoRequest.of(MenuCode.COMMUNITY.name(), MenuCode.COMMUNITY.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_FREE.name(),MenuCode.SUB_FREE.getKo(), community.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_MARKET.name(),MenuCode.SUB_MARKET.getKo(), community.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_QNA.name(),MenuCode.SUB_QNA.getKo(), community.id()));

        // complaint
        MenuDtoResponse complaint = menuService.createMenu(MenuDtoRequest.of(MenuCode.COMPLAINT.name(), MenuCode.COMPLAINT.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_COMPLAINT.name(),MenuCode.SUB_COMPLAINT.getKo(), complaint.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_MYCOMPLAINT.name(),MenuCode.SUB_MYCOMPLAINT.getKo(), complaint.id()));

        // fee
        MenuDtoResponse fee = menuService.createMenu(MenuDtoRequest.of(MenuCode.FEE.name(), MenuCode.FEE.getKo(), null));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_TOTALFEE.name(),MenuCode.SUB_TOTALFEE.getKo(), fee.id()));
        menuService.createMenu(MenuDtoRequest.of(MenuCode.SUB_MYFEE.name(),MenuCode.SUB_MYFEE.getKo(), fee.id()));


//        categoryService.createCategory(2L, MenuDtoRequest.of("엘리베이터"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("공동생활"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("주차장"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("공동현관/복도"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("보안/경비"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("조명"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("조경"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("커뮤니티시설"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("시공사하자"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("도로/인도"));
//        categoryService.createCategory(2L, MenuDtoRequest.of("기타"));

    }
}
