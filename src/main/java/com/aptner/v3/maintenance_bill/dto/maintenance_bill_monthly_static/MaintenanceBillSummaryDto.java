package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import com.aptner.v3.maintenance_bill.dto.maintenance_bill.FareCollectionDiscountDto;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill.FareCollectionDiscount;
import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class MaintenanceBillSummaryDto {
    LocalDate targetDate;
    private long beforeDeadlineFee;
    private int fareCollectionTotalDiscount;
    private FareCollectionDiscountDto fareCollectionDiscount;
    private int unpaidFee;
    private int afterDeadlineFee;

    public void calculateFareCollectionTotalDiscount() {
        fareCollectionTotalDiscount
                += fareCollectionDiscount.getMaintenanceDiscount()
                + fareCollectionDiscount.getHiringDiscount()
                + fareCollectionDiscount.getSummerElectricityDiscount()
                + fareCollectionDiscount.getParkingFeeDiscount()
                + fareCollectionDiscount.getVoucherDiscount()
                + fareCollectionDiscount.getElectricityDiscount()
                + fareCollectionDiscount.getWaterDiscount();
    }
}