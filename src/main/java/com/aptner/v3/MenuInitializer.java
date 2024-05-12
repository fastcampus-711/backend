package com.aptner.v3;

import com.aptner.v3.board.category.CategoryService;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.menu.MenuName;
import com.aptner.v3.menu.MenuService;
import com.aptner.v3.menu.dto.MenuDto;
import com.aptner.v3.user.User;
import com.aptner.v3.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MenuInitializer implements CommandLineRunner {

    private final MenuService menuService;
    private final CategoryService categoryService;
    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {

        userRepository.save(new User("user@aptner.com", "user", "12345678!"));

        menuService.createMenu(new MenuDto.MenuRequest(MenuName.INFO.name(), MenuName.INFO.getKo()));
        menuService.createMenu(new MenuDto.MenuRequest(MenuName.NOTICE.name(), MenuName.NOTICE.getKo()));
        menuService.createMenu(new MenuDto.MenuRequest(MenuName.MANDATORY.name(), MenuName.MANDATORY.getKo()));
        menuService.createMenu(new MenuDto.MenuRequest(MenuName.COMMUNITY.name(), MenuName.COMMUNITY.getKo()));
        menuService.createMenu(new MenuDto.MenuRequest(MenuName.COMPLAINT.name(), MenuName.COMPLAINT.getKo()));
        menuService.createMenu(new MenuDto.MenuRequest(MenuName.FEE.name(), MenuName.FEE.getKo()));

//        menuService.updateMenu(2L, new MenuDto.Request(MenuName.NOTICE.name(), "공지사항2"));

        // remove
        categoryService.createCategory(1L, new CategoryDto.Request("인사말"));
        categoryService.createCategory(1L, new CategoryDto.Request("단지전경"));
        categoryService.createCategory(1L, new CategoryDto.Request("연락처 정보"));
        categoryService.createCategory(1L, new CategoryDto.Request("커뮤니티 시설"));
        categoryService.createCategory(2L, new CategoryDto.Request("공지사항"));
        categoryService.createCategory(2L, new CategoryDto.Request("일정표"));
        categoryService.createCategory(3L, new CategoryDto.Request("관리비"));
        categoryService.createCategory(3L, new CategoryDto.Request("계약서"));
        categoryService.createCategory(3L, new CategoryDto.Request("관리규약"));
        categoryService.createCategory(3L, new CategoryDto.Request("장기수선충당금"));
        categoryService.createCategory(3L, new CategoryDto.Request("안전관리계획"));
        categoryService.createCategory(3L, new CategoryDto.Request("입찰정보"));
        categoryService.createCategory(4L, new CategoryDto.Request("자유게시판"));
        categoryService.createCategory(4L, new CategoryDto.Request("나눔장터"));
        categoryService.createCategory(4L, new CategoryDto.Request("QnA"));
        categoryService.createCategory(5L, new CategoryDto.Request("전체민원"));
        categoryService.createCategory(5L, new CategoryDto.Request("나의민원"));
        categoryService.createCategory(6L, new CategoryDto.Request("전체조회"));
        categoryService.createCategory(6L, new CategoryDto.Request("나의관리비"));

//        categoryService.createCategory(2L, new CategoryDto.Request("엘리베이터"));
//        categoryService.createCategory(2L, new CategoryDto.Request("공동생활"));
//        categoryService.createCategory(2L, new CategoryDto.Request("공동현관/복도"));
//        categoryService.createCategory(2L, new CategoryDto.Request("주차장"));
//        categoryService.createCategory(2L, new CategoryDto.Request("보안/경비"));
//        categoryService.createCategory(2L, new CategoryDto.Request("조명"));
//        categoryService.createCategory(2L, new CategoryDto.Request("조경"));
//        categoryService.createCategory(2L, new CategoryDto.Request("커뮤니티시설"));
//        categoryService.createCategory(2L, new CategoryDto.Request("시공사하자"));
//        categoryService.createCategory(2L, new CategoryDto.Request("도로/인도"));
//        categoryService.createCategory(2L, new CategoryDto.Request("기타"));

    }
}
