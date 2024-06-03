package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class EnergyUsageDto {
    private int electricityUsage;
    private int waterUsage;
    private int hotWaterUsage;
    private int heatingUsage;
    private int gasUsage;

    private int previousMonthElectricityGuideline;
    private int currentMonthElectricityGuideline;
    private int previousMonthWaterGuideline;
    private int currentMonthWaterGuideline;
    private int previousMonthHotWaterGuideline;
    private int currentMonthHotWaterGuideline;
    private int previousMonthHeatingGuideline;
    private int currentMonthHeatingGuideline;
    private int previousMonthGasGuideline;
    private int currentMonthGasGuideline;
}
