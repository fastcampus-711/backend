package com.aptner.v3.menu.repository.querydsl;

import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.QMenu;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class MenuRepositoryImpl
//        implements MenuRepositoryCustom {
{
//    @Autowired
//    private JPAQueryFactory queryFactory;
//
//    @Override
//    public List<Menu> findByIdOrParentIdUsingQuerydsl(Long id, Long parentId) {
//        QMenu menu = QMenu.menu;
//        return queryFactory
//                .selectFrom(menu)
//                .where(
//                        menu.id.eq(id).or(menu.parentId.eq(parentId))
//                ).fetch();
//    }
//
//    @Override
//    public List<Menu> findAllActiveWithActiveParentUsingQuerydsl() {
//
//        QMenu menu = QMenu.menu;
//        QMenu other = QMenu.menu; // alias
//
//        return queryFactory
//                .selectFrom(menu)
//                .leftJoin(other).on(other.id.eq(menu.parentId))
//                .where(menu.deleted.isFalse()
//                        .and(other.deleted.isFalse().or(menu.parentId.isNull())))
//                .fetch();
//    }
}
