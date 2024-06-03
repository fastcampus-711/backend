package com.aptner.v3.maintenance_bill.repository;

import com.aptner.v3.maintenance_bill.domain.MaintenanceBillMonthlyStatic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

public interface MaintenanceBillMonthlyStaticRepository extends JpaRepository<MaintenanceBillMonthlyStatic, Long> {
    @Query("SELECT m FROM MaintenanceBillMonthlyStatic m WHERE m.id.houseId=:houseId and m.id.impositionDate=:targetDate")
    Optional<MaintenanceBillMonthlyStatic> findByHouseIdAndTargetDate(@Param("houseId") long houseId, @Param("targetDate") LocalDate targetDate);
}
