package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FareCollectionDiscountDto {
    private int maintenanceDiscount;
    private int hiringDiscount;
    private int summerElectricityDiscount;
    private int parkingFeeDiscount;
    private int voucherDiscount;
    private int electricityDiscount;
    private int waterDiscount;
}
