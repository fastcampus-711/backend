package com.aptner.v3.global.config;

import com.aptner.v3.menu.MenuItem;
import com.aptner.v3.menu.MenuItemRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class MenuInitializer implements CommandLineRunner {

    private final MenuItemRepository menuItemRepository;
    Map<String, MenuItem> list = new HashMap<>();

    public MenuInitializer(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @Override
    public void run(String... args) throws Exception {

        addMenuItem(null, "SERVICE_CENTER", "고객센터");
        addMenuItem(list.get("SERVICE_CENTER"), "DIRECT_INQUIRY", "1:1 문의");

        addMenuItem(null, "BOARD_NOTICE", "공지사항");
        addMenuItem(list.get("BOARD_NOTICE"), "NOTICE", "공지사항");

        addMenuItem(null, "SERVICE_MANAGEMENT", "서비스관리");
        addMenuItem(list.get("SERVICE_MANAGEMENT"), "CONTRACT_INFORMATION", "계약정보");
        addMenuItem(list.get("SERVICE_MANAGEMENT"), "POPUP_SETTING", "팝업 설정");
        addMenuItem(list.get("SERVICE_MANAGEMENT"), "SERVICE_SETTING", "서비스 기본 설정");

        addMenuItem(null, "APARTMENT_INFORMATION_MANAGEMENT", "아파트 정보");
        addMenuItem(list.get("APARTMENT_INFORMATION_MANAGEMENT"), "APARTMENT_INFORMATION", "아파트 기본정보");
        addMenuItem(list.get("APARTMENT_INFORMATION_MANAGEMENT"), "CONTACT_INFORMATION", "연락처정보");
        addMenuItem(list.get("APARTMENT_INFORMATION_MANAGEMENT"), "FEE_RECEIVING_BANKS", "관리비 수납은행");
        addMenuItem(list.get("APARTMENT_INFORMATION_MANAGEMENT"), "FEE_FILE_LIST", "관리비 업로드");
        addMenuItem(list.get("APARTMENT_INFORMATION_MANAGEMENT"), "APARTMENT_INTRODUCE", "아파트 소개");

        addMenuItem(null, "TENANT_JOIN_STATUS", "입주자 정보");
        addMenuItem(list.get("TENANT_JOIN_STATUS"), "TENANT_JOIN_STATUS", "입주자 현황정보");
        addMenuItem(list.get("TENANT_JOIN_STATUS"), "TENANT_LIST", "입주자 명부");
        addMenuItem(list.get("TENANT_JOIN_STATUS"), "TENANT_COMMITTEES", "입주자자치기구");

        addMenuItem(null, "FEE", "관리비");
        addMenuItem(list.get("FEE"), "FEE_CHARGE_DETAILS", "부과내역");

        addMenuItem(null, "RESERVATION_MANAGEMENT", "분양 관리");
        addMenuItem(list.get("RESERVATION_MANAGEMENT"), "PRE_INSPECTION_RESERVATIONS", "사전 점검 예약");
        addMenuItem(list.get("RESERVATION_MANAGEMENT"), "MOVE_IN_RESERVATIONS", "입주 예약");

        // @동적 변경
        addMenuItem(null, "BOARD_COMMUNITY", "소통공간");
        addMenuItem(list.get("BOARD_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS1", "자유게시판");
        addMenuItem(list.get("BOARD_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS2", "나눔장터");
        addMenuItem(list.get("BOARD_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS3", "동호회 모집");
        addMenuItem(list.get("BOARD_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS4", "맛집 추천");
        addMenuItem(list.get("BOARD_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS5", "분실물");
        addMenuItem(list.get("BOARD_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS6", "독서모임");

        addMenuItem(null, "BOARD_COMPLAINT", "아파트 민원");
        addMenuItem(list.get("BOARD_COMPLAINT"), "APARTMENT_COMPLAINTS", "아파트 민원");

        addMenuItem(null, "VOTE", "주민투표");
        addMenuItem(list.get("VOTE"), "TENANT_VOTES", "주민투표");
        addMenuItem(list.get("VOTE"), "DAILY_VOTE_SMS_LOGS", "일자별 SMS 발송 내역");
        addMenuItem(list.get("VOTE"), "ON_SITE_VOTE_MANAGEMENT_ACCOUNTS", "현장투표 관리자");

        addMenuItem(null, "POLL", "설문조사");
        addMenuItem(list.get("POLL"), "POLL_LIST", "설문조사");

        addMenuItem(null, "VISIT_CAR", "방문차량");
        addMenuItem(list.get("VISIT_CAR"), "VISIT_CAR_RESERVATION_DETAILS", "방문차량 예약내역");
        addMenuItem(list.get("VISIT_CAR"), "VISIT_CAR_INOUT_HISTORY", "입출차내역");

        addMenuItem(null, "AUTOGOV", "자치단체");
        addMenuItem(list.get("AUTOGOV"), "AUTOGOV_DOCUMENTS", "자치단체공문");
        addMenuItem(list.get("AUTOGOV"), "AUTOGOV_NOTICES", "자치단체공지");

        addMenuItem(null, "E_APPROVAL", "전자결재");
        addMenuItem(list.get("E_APPROVAL"), "NORMAL_REQUESTS", "작성 품의서");
        addMenuItem(list.get("E_APPROVAL"), "APPROVAL_REQUESTS", "결재 품의서");
        addMenuItem(list.get("E_APPROVAL"), "REFERENCE_REQUESTS", "참조 품의서");

        addMenuItem(null, "APARTMENT_BROADCASTING", "아파트 방송");
        addMenuItem(list.get("APARTMENT_BROADCASTING"), "APARTMENT_BROADCASTING_REGISTER", "방송 등록");
        addMenuItem(list.get("APARTMENT_BROADCASTING"), "APARTMENT_BROADCASTING_TEMPLATES", "방송 서식");
        addMenuItem(list.get("APARTMENT_BROADCASTING"), "APARTMENT_BROADCASTING_RESERVATIONS", "예약 방송 내역");
        addMenuItem(list.get("APARTMENT_BROADCASTING"), "APARTMENT_BROADCASTING_HISTORY", "지난 방송 내역");

        // front menu
        addMenuItem(null, "FRONT_INTRO", "아파트 소개",2);
        addMenuItem(list.get("FRONT_INTRO"), "INFO", "인사말",2);
        addMenuItem(list.get("FRONT_INTRO"), "INTRO", "단지전경",2);
        addMenuItem(list.get("FRONT_INTRO"), "CONTACT", "연락처정보",2);
        addMenuItem(list.get("FRONT_INTRO"), "COMMUNITY", "커뮤니티시설",2);

        // category로 대체
        addMenuItem(null, "FRONT_NOTICE", "공지사항",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "공지사항",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "공동생활",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "공사안내",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "관리비",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "관리규약",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "장기수선계획",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "안전관리계획",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "입찰공고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "계약서",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "감사보고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "소집보고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS", "회의결과",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "투표공고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "투표결과",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "선거공고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "후보자공고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "선거결과",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "사퇴공고",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "원격지원방송",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "기타",2);
        addMenuItem(list.get("FRONT_NOTICE"), "COMMUNITY_DYNAMIC_BOARDS6", "1111",2);

        addMenuItem(null, "FRONT_DUTY", "의무공개", 2);

        addMenuItem(null, "FRONT_COMMUNITY", "소통공간", 2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "자유게시판",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "나눔장터",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "동호회모집",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "맛집추천",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "분실물",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "독서모임",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "8조 게시판 개발완료",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "7조 테스트",2);
        addMenuItem(list.get("FRONT_COMMUNITY"), "COMMUNITY_DYNAMIC_BOARDS", "101동 게시판",2);
    }

    private void addMenuItem(MenuItem parent, String code, String name, int pageRole) {
        MenuItem menuItem = new MenuItem(parent, code, name, pageRole);
        menuItem = menuItemRepository.save(menuItem);
        list.put(code, menuItem);
    }

    private void addMenuItem(MenuItem parent, String code, String name) {
        MenuItem menuItem = new MenuItem(parent, code, name);
        menuItem = menuItemRepository.save(menuItem);
        list.put(code, menuItem);
    }
}
