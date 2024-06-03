package com.aptner.v3.maintenance_bill.domain;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseAverageMaintenanceFee {
    @EmbeddedId
    private HouseAverageMaintenanceFeePrimaryKeys id;

    private long maxMaintenanceFee;
    private long averageMaintenanceFee;
    private long minMaintenanceFee;

    private long averageElectricityUsage;
    private long averageElectricityFee;

    private long averageWaterUsage;
    private long averageWaterFee;

    private long averageHotWaterUsage;
    private long averageHotWaterFee;

    private long averageHeatingUsage;
    private long averageHeatingFee;

    public HouseAverageMaintenanceFee(int code,
                                      double squareMeter,
                                      LocalDate targetDate,
                                      long maxMaintenanceFee,
                                      double averageMaintenanceFee,
                                      long minMaintenanceFee,
                                      double averageElectricityUsage,
                                      double averageElectricityFee,
                                      double averageWaterUsage,
                                      double averageWaterFee,
                                      double averageHotWaterUsage,
                                      double averageHotWaterFee,
                                      double averageHeatingUsage,
                                      double averageHeatingFee) {
        this.id = HouseAverageMaintenanceFeePrimaryKeys.builder()
                .houseCode(code)
                .squareMeter(squareMeter)
                .targetDate(targetDate)
                .build();
        this.maxMaintenanceFee = maxMaintenanceFee;
        this.averageMaintenanceFee = (long) averageMaintenanceFee;
        this.minMaintenanceFee = minMaintenanceFee;
        this.averageElectricityUsage = (long) averageElectricityUsage;
        this.averageElectricityFee = (long) averageElectricityFee;
        this.averageWaterUsage = (long) averageWaterUsage;
        this.averageWaterFee = (long) averageWaterFee;
        this.averageHotWaterUsage = (long) averageHotWaterUsage;
        this.averageHotWaterFee = (long) averageHotWaterFee;
        this.averageHeatingUsage = (long) averageHeatingUsage;
        this.averageHeatingFee = (long) averageHeatingFee;
    }
}
