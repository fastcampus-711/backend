package com.aptner.v3.maintenance_bill.domain;

import com.aptner.v3.maintenance_bill.embed.maintenance_bill_monthly_static.*;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceBillMonthlyStatic {
    @EmbeddedId
    private MaintenanceBillPrimaryKeys id;

    @Embedded
    private CircularChart circularChart;
    @Embedded
    private MaintenanceBillSummary maintenanceBillSummary;

    @Embedded
    private MonthOnMonthInfo monthOnMonthInfo;

    @Embedded
    private YearOnYearInfo yearOnYearInfo;

    @Embedded
    private SquareOnSquareInfo squareOnSquareInfo;

    @ElementCollection
    @CollectionTable(name = "ENERGY_USAGE",
            joinColumns = {@JoinColumn(name = "house_id"), @JoinColumn(name = "imposition_date")})
    private List<EnergyUsage> energyUsages;
}
