package com.aptner.v3.menu.repository.querydsl;

import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.List;

@Qualifier
public class MenuRepositoryImpl implements MenuRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<Menu> findByIdOrParentId2(Long id, Long parentId) {
        QMenu menu = QMenu.menu;
        return queryFactory
                .selectFrom(menu)
                .where(
                        menu.id.eq(id).or(menu.parentId.eq(parentId))
                ).fetch();
    }

    @Override
    public List<Menu> findAllActiveWithActiveParent2() {

        QMenu menu = QMenu.menu;

        return queryFactory
                .selectFrom(menu)
                .where(menu.deleted.eq(false)
                        .and(menu.parentId.isNotNull())
                        .and(menu.id.in(
                                queryFactory.select(menu.parentId)
                                        .from(menu)
                                        .where(menu.deleted.eq(false))
                        )))
                .fetch();
    }
}
