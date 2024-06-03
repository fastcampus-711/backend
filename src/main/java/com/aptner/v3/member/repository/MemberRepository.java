package com.aptner.v3.member.repository;

import com.aptner.v3.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Optional<Member> findByUsername(String username);

    @Query("SELECT m FROM users m WHERE m.residentInfo.house.id=:houseId")
    Optional<Member> findByHouseId(@Param("houseId") long houseId);
}
