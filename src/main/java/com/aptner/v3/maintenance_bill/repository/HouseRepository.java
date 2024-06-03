package com.aptner.v3.maintenance_bill.repository;

import com.aptner.v3.maintenance_bill.domain.House;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
    Optional<House> findByCodeAndDongAndHo(int code, String dong, String ho);
}
