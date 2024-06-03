package com.aptner.v3.maintenance_bill.repository;

import com.aptner.v3.maintenance_bill.domain.HouseAverageMaintenanceFee;
import com.aptner.v3.maintenance_bill.domain.MaintenanceBill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MaintenanceBillRepository extends JpaRepository<MaintenanceBill, Long> {
    @Query("SELECT b FROM MaintenanceBill b WHERE b.id.impositionDate=:targetDate")
    List<MaintenanceBill> findByImpositionDate(@Param("targetDate") LocalDate targetDate);

    @Query("SELECT b.currentMonthFee FROM MaintenanceBill b " +
            "WHERE b.id.houseId=:houseId AND b.id.impositionDate=:targetDate")
    Optional<Long> getCurrentMonthFeeFindByHouseIdAndImpositionDate(@Param("houseId") long houseId, @Param("targetDate") LocalDate targetDate);

    @Query("SELECT b FROM MaintenanceBill b " +
            "WHERE b.id.houseId=:houseId AND b.id.impositionDate=:targetDate")
    Optional<MaintenanceBill> findByHouseIdAndImpositionDate(@Param("houseId") long houseId, @Param("targetDate") LocalDate targetDate);

    @Query("SELECT " +
            "new HouseAverageMaintenanceFee(h.code, h.squareMeter, m.id.impositionDate, " +
            "MAX(m.currentMonthFee), AVG(m.currentMonthFee), MIN(m.currentMonthFee), " +
            "AVG(m.energyUsage.electricityUsage), AVG(m.fareCollectionFee.householdElectricityFee), " +
            "AVG(m.energyUsage.waterUsage), AVG(m.fareCollectionFee.householdWaterFee), " +
            "AVG(m.energyUsage.hotWaterUsage), AVG(m.fareCollectionFee.householdHotWaterFee)," +
            "AVG(m.energyUsage.heatingUsage), AVG(m.fareCollectionFee.householdHeatingFee)) " +
            "FROM MaintenanceBill m left join House h ON m.id.houseId = h.id " +
            "WHERE h.code=:code and h.squareMeter=:squareMeter and m.id.impositionDate=:targetDate")
    HouseAverageMaintenanceFee staticByHouseCodeAndHouseSquareMeterAndTargetDate(
            @Param("code") int code, @Param("squareMeter") double squareMeter, @Param("targetDate") LocalDate targetDate
    );

    @Query("SELECT m FROM MaintenanceBill m LEFT JOIN House h ON m.id.houseId = h.id WHERE h.id=:houseId and m.id.impositionDate=:targetDate")
    Optional<MaintenanceBill> findByHouseIdAndTargetDate(@Param("houseId") long houseId, @Param("targetDate") LocalDate targetDate);
}
