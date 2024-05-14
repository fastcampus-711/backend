package com.aptner.v3.menu.repository;

import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.repository.querydsl.MenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> , MenuRepositoryCustom {

    @Query("SELECT m FROM Menu m WHERE m.id = :id OR m.parentId = :parentId")
    List<Menu> findByIdOrParentId(@Param("id") long id, @Param("parentId") Long parentId);

    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN Menu parent ON m.parentId = parent.id " +
            "WHERE m.deleted = false AND (parent.deleted = false OR m.parentId IS NULL)")
    List<Menu> findAllActiveWithActiveParent();
}
