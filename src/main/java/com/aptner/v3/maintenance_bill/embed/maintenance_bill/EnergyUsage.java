package com.aptner.v3.maintenance_bill.embed.maintenance_bill;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnergyUsage {
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
