package com.aptner.v3.global.util;

import com.aptner.v3.menu.Menu;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public final class MenuUtil {

    /**
     * 부모 목록 추출
     *
     * @param menuList 전체 메뉴
     * @return 부모 목록
     */
    public static List<Menu> getParent(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> item.getParentId() == null)
                .collect(Collectors.toList());
    }

    /**
     * 자식 목록 추출
     *
     * @param menuList 전체 메뉴
     * @return 자식 목록
     */
    public static Map<Long, List<Menu>> getChild(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> item.getParentId() != null)
                .collect(Collectors.groupingBy(Menu::getParentId));
    }
}
