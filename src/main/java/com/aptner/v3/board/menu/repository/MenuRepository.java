package com.aptner.v3.board.menu.repository;

import com.aptner.v3.board.menu.domain.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface MenuRepository extends JpaRepository<Menu, Long> {
    @Modifying
    @Query("UPDATE Menu b SET b.name = ?2 WHERE b.id = ?1")
    void updateName(long target, String to);

}
