package com.aptner.v3.maintenance_bill.repository;

import com.aptner.v3.maintenance_bill.domain.HouseAverageMaintenanceFee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface HouseAverageMaintenanceFeeRepository extends JpaRepository<HouseAverageMaintenanceFee, Long> {

    @Query("SELECT h FROM HouseAverageMaintenanceFee h " +
            "WHERE h.id.houseCode=:code AND h.id.squareMeter=:squareMeter AND h.id.targetDate=:targetDate")
    Optional<HouseAverageMaintenanceFee> findByHouseCodeAndHouseSquareMeterAndTargetDate(
            @Param("code") int code, @Param("squareMeter") double squareMeter, @Param("targetDate") LocalDate targetDate
    );
}
