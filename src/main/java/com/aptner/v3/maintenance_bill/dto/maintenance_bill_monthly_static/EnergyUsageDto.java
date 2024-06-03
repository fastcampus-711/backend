package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import com.aptner.v3.maintenance_bill.domain.type.EnergyType;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EnergyUsageDto {
    private EnergyType energyType;

    private int presentUsage;
    private int presentFee;

    private int lastYearUsage;
    private int lastYearFee;

    private int averageUsageOfSameSquare;
    private int averageFeeOfSameSquare;
}
