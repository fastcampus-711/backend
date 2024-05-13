package com.aptner.v3.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MenuRepository extends JpaRepository<Menu, Long> {

    @Query("SELECT m FROM Menu m WHERE m.id = :id OR m.parentId = :parentId")
    List<Menu> findByIdOrParentId(@Param("id") long id, @Param("parentId") Long parentId);

    @Query("SELECT m FROM Menu m " +
            "LEFT JOIN Menu parent ON m.parentId = parent.id " +
            "WHERE m.deleted = false AND (parent.deleted = false OR parent IS NULL)")
    List<Menu> findAllActiveWithActiveParent();
}
