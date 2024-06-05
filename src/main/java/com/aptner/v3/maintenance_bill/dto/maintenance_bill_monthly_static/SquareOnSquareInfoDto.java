package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SquareOnSquareInfoDto {
    private long maintenanceFeeOfPresent;
    private long minMaintenanceFeeOfSameSquares;
    private long averageMaintenanceFeeOfSameSquares;
    private long maxMaintenanceFeeOfSameSquares;
}
