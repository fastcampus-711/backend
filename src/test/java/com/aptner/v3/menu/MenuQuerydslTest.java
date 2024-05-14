package com.aptner.v3.menu;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@Transactional
public class MenuQuerydslTest {

    @Autowired
    JPAQueryFactory queryFactory;

    @Test
    public void search() {

        Menu findMember = queryFactory
                .select(QMenu.menu)
                .from(QMenu.menu)
                .where(
                        QMenu.menu.id.eq(1L)
                ).fetchOne();

        assertThat(findMember.getId()).isEqualTo(1L);
    }

    @Test
    public void searchAndParam() {
        List<Menu> result = queryFactory
                .selectFrom(QMenu.menu)
                .where(QMenu.menu.id.eq(6L).or(QMenu.menu.parentId.eq(6L)))
                .fetch();

        assertThat(result.stream().count()).isEqualTo(3);
    }

    @Test
    public void Join_on_filter() {
        QMenu menu = QMenu.menu;
        QMenu other = QMenu.menu; // alias

        List<Menu> result = queryFactory
                .selectFrom(menu)
                .leftJoin(other).on(other.id.eq(menu.parentId))
                .where(menu.deleted.isFalse()
                        .and(other.deleted.isFalse().or(menu.parentId.isNull())))
                .fetch();

        long previousCount = result.stream().count();

        queryFactory.delete(menu)
                .where(menu.id.eq(6L))
                .execute();

        result = queryFactory
                .selectFrom(menu)
                .leftJoin(other).on(other.id.eq(menu.parentId))
                .where(menu.deleted.isFalse()
                        .and(other.deleted.isFalse().or(menu.parentId.isNull())))
                .fetch();

        assertThat(result.stream().count()).isEqualTo(previousCount - 1);

    }
}
