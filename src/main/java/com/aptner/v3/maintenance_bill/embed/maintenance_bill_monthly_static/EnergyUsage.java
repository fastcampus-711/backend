package com.aptner.v3.maintenance_bill.embed.maintenance_bill_monthly_static;

import com.aptner.v3.maintenance_bill.domain.type.EnergyType;
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
    private EnergyType energyType;

    private int presentUsage;
    private int presentFee;

    private int lastYearUsage;
    private int lastYearFee;

    private long averageUsageOfSameSquare;
    private long averageFeeOfSameSquare;
}
