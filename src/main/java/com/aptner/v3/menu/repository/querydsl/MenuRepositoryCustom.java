package com.aptner.v3.menu.repository.querydsl;

import com.aptner.v3.menu.Menu;

import java.util.List;

public interface MenuRepositoryCustom {
    List<Menu> findByIdOrParentId(Long id, Long parentId);
    List<Menu> findAllActiveWithActiveParent();
}
