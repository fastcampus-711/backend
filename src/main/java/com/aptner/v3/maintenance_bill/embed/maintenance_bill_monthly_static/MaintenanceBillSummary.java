package com.aptner.v3.maintenance_bill.embed.maintenance_bill_monthly_static;

import com.aptner.v3.maintenance_bill.embed.maintenance_bill.FareCollectionDiscount;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MaintenanceBillSummary {
    private long beforeDeadlineFee;
    private FareCollectionDiscount fareCollectionDiscount;
    private int unpaidFee;
    private long afterDeadlineFee;
}
