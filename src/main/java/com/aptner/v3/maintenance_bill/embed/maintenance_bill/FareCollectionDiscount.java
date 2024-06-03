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
public class FareCollectionDiscount {
    private int fareCollectionTotalDiscount;
    private int maintenanceDiscount;
    private int hiringDiscount;
    private int summerElectricityDiscount;
    private int parkingFeeDiscount;
    private int voucherDiscount;
    private int electricityDiscount;
    private int waterDiscount;
}
