package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class YearOnYearInfoDto {
    private long maintenanceFeeOfPresent;
    private long maintenanceFeeOfLastYear;
}
