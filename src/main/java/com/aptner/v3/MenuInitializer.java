package com.aptner.v3;

import com.aptner.v3.auth.dto.LoginDto;
import com.aptner.v3.auth.dto.TokenDto;
import com.aptner.v3.auth.service.AuthService;
import com.aptner.v3.board.category.BoardGroup;
import com.aptner.v3.board.category.CategoryService;
import com.aptner.v3.board.category.dto.CategoryDto;
import com.aptner.v3.maintenance_bill.domain.House;
import com.aptner.v3.maintenance_bill.domain.type.HouseType;
import com.aptner.v3.maintenance_bill.repository.HouseRepository;
import com.aptner.v3.member.Member;
import com.aptner.v3.member.MemberRole;
import com.aptner.v3.member.repository.MemberRepository;
import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.MenuCode;
import com.aptner.v3.menu.MenuService;
import com.aptner.v3.menu.dto.MenuDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.List;

import static com.aptner.v3.CommunityApplication.passwordEncoder;

@Component
public class MenuInitializer implements CommandLineRunner {

    private final MenuService menuService;
    private final CategoryService categoryService;
    private final MemberRepository memberRepository;
    private final HouseRepository houseRepository;

    private final AuthService authService;
    private final Boolean isTest;
    private final Environment environment;

    public MenuInitializer(MenuService menuService, CategoryService categoryService,
                           MemberRepository memberRepository, AuthService authService,
                           @Value("${server.auth}") Boolean isTest,
                           HouseRepository houseRepository,
                            Environment environment) {
        this.menuService = menuService;
        this.categoryService = categoryService;
        this.memberRepository = memberRepository;
        this.authService = authService;
        this.isTest = isTest;
        this.houseRepository = houseRepository;
        this.environment = environment;
    }

    @Override
    public void run(String... args) {

        String ddlAuto = environment.getProperty("spring.jpa.hibernate.ddl-auto");
        if (!"create-drop".equals(ddlAuto)) {
        // ddl-auto가 create-drop이 아닌 경우 실행 중단
            return;
        }

        House house = House.of(77777, "패캠세븐아파트", HouseType.APARTMENT,115.7,  "701", "103");
        houseRepository.save(house);
        Member user = memberRepository.save(Member.of("user", passwordEncoder().encode("p@ssword"), "nickname1", "https://avatars.githubusercontent.com/u/79270228?v=4", "01011112222", List.of(MemberRole.ROLE_USER), house));

        // intro
        Menu intro = menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOP_INFO.name(), MenuCode.TOP_INFO.getKo(), null));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.INTRO.name(), MenuCode.INTRO.getKo(), intro.getId()));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.APT.name(), MenuCode.APT.getKo(), intro.getId()));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.CONTACT.name(), MenuCode.CONTACT.getKo(), intro.getId()));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.COMMUNITY.name(), MenuCode.COMMUNITY.getKo(), intro.getId()));

        // notice
        Menu notice = menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOP_NOTICE.name(), MenuCode.TOP_NOTICE.getKo(), null));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(notice.getId(), MenuCode.NOTICE.name(), MenuCode.NOTICE.getKo(), BoardGroup.NOTICES));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.SCHEDULE.name(), MenuCode.SCHEDULE.getKo(), notice.getId()));

        // mandatory
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOP_MANDATORY.name(), MenuCode.TOP_MANDATORY.getKo(), null));

        // community
        Menu community = menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOP_COMMUNITY.name(), MenuCode.TOP_COMMUNITY.getKo(), null));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(community.getId(), MenuCode.FREE.name(), MenuCode.FREE.getKo(), BoardGroup.FREES));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(community.getId(), MenuCode.MARKET.name(), MenuCode.MARKET.getKo(), BoardGroup.MARKETS));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(community.getId(), MenuCode.QNA.name(), MenuCode.QNA.getKo(), BoardGroup.QNAS));

        // complaint
        Menu complaint = menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOP_COMPLAINT.name(), MenuCode.TOP_COMPLAINT.getKo(), null));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(complaint.getId(), MenuCode.COMPLAINT.name(), MenuCode.COMPLAINT.getKo(), BoardGroup.COMPLAINT));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(complaint.getId(), MenuCode.MYCOMPLAINT.name(), MenuCode.MYCOMPLAINT.getKo(), BoardGroup.MYCOMPLAINT));

        // fee
        Menu fee = menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOP_FEE.name(), MenuCode.TOP_FEE.getKo(), null));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.TOTALFEE.name(), MenuCode.TOTALFEE.getKo(), fee.getId()));
        menuService.createMenu(MenuDto.MenuDtoRequest.of(MenuCode.MYFEE.name(), MenuCode.MYFEE.getKo(), fee.getId()));

        // 공지사항(의무) 분류
        categoryService.createCategory(CategoryDto.CategoryRequest.of("공동생활", "1", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("공사안내", "2", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("선거관리위원회", "3", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("입주자대표회의", "4", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("관리비", "5", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("계약서", "6", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("관리규약", "7", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("장기수선충당금", "8", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("안전관리계획", "9", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("입찰정보", "10", BoardGroup.NOTICES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("기타", "11", BoardGroup.NOTICES));
        // 자유 게시판 분류
        categoryService.createCategory(CategoryDto.CategoryRequest.of("취미/운동", "12", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("생활/편의", "13", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("음식/카페", "14", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("병원/약국", "15", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("수리/시공", "16", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("투자/부동산", "17", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("교육/육아", "18", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("아파트/동네소식", "19", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("여행", "20", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("살림정보", "21", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("모임/동호회", "22", BoardGroup.FREES));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("기타", "23", BoardGroup.FREES));
        // QNA 게시판 분류
        categoryService.createCategory(CategoryDto.CategoryRequest.of("QNA", "24", BoardGroup.QNAS));
        // 나눔 장터 게시판 분류
        categoryService.createCategory(CategoryDto.CategoryRequest.of("무료나눔", "25", BoardGroup.MARKETS));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("중고거래", "26", BoardGroup.MARKETS));
        // 민원 게시판 분류
        categoryService.createCategory(CategoryDto.CategoryRequest.of("엘리베이터", "27", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("공동생활", "28", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("공동현관/복도", "29", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("주차장", "30", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("보안경비", "31", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("조명", "32", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("조경", "33", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("커뮤니티시설", "34", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("시공사하자", "35", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("도로/인도", "36", BoardGroup.COMPLAINT));
        categoryService.createCategory(CategoryDto.CategoryRequest.of("기타", "37", BoardGroup.COMPLAINT));


        // 가짜 로그인 사용자 지정
        if(!isTest) {
            TokenDto login = authService.login(LoginDto.builder()
                    .password("p@ssword")
                    .username("user")
                    .build());
            System.out.println(login.accessToken());
        }
    }
}
