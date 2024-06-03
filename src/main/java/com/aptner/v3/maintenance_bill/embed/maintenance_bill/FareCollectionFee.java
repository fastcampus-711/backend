package com.aptner.v3.maintenance_bill.embed.maintenance_bill;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FareCollectionFee {
    private long fareCollectionTotalFee;
    private int municipalWasteFee;
    private int electionCommissionFee;
    private int suspendReceipt;
    private int householdElectricityFee;
    private int commonElectricityFee;
    private int householdWaterFee;
    private int commonWaterFee;
    private int householdHotWaterFee;
    private int commonHotWaterFee;
    private int householdHeatingFee;
    private int liftElectricityFee;
    private int tvLicenseFee;
    private int sewageFee;

    @Embedded
    private FareCollectionDiscount fareCollectionDiscount;
}
