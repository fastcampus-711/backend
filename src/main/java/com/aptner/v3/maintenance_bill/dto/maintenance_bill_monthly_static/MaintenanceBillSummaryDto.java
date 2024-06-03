package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import com.aptner.v3.maintenance_bill.dto.maintenance_bill.FareCollectionDiscountDto;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill.FareCollectionDiscount;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MaintenanceBillSummaryDto {
    private long beforeDeadlineFee;
    private FareCollectionDiscountDto fareCollectionDiscount;
    private int unpaidFee;
    private int afterDeadlineFee;
}
