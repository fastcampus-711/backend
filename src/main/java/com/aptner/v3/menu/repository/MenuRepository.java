package com.aptner.v3.menu.repository;

import com.aptner.v3.menu.Menu;
import com.aptner.v3.menu.repository.querydsl.MenuRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuRepository extends JpaRepository<Menu, Long> , MenuRepositoryCustom {

}
