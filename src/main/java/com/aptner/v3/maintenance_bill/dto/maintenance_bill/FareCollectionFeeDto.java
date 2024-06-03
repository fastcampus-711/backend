package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class FareCollectionFeeDto {
    private int fareCollectionTotalFee;
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

    private FareCollectionDiscountDto fareCollectionDiscount;

    public FareCollectionFeeDto(int fareCollectionTotalFee,
                                int municipalWasteFee,
                                int electionCommissionFee,
                                int suspendReceipt,
                                int householdElectricityFee,
                                int commonElectricityFee,
                                int householdWaterFee,
                                int commonWaterFee,
                                int householdHotWaterFee,
                                int commonHotWaterFee,
                                int householdHeatingFee,
                                int liftElectricityFee,
                                int tvLicenseFee,
                                int sewageFee,
                                int maintenanceDiscount,
                                int hiringDiscount,
                                int summerElectricityDiscount,
                                int parkingFeeDiscount,
                                int voucherDiscount,
                                int electricityDiscount,
                                int waterDiscount) {
        this.fareCollectionTotalFee = fareCollectionTotalFee;
        this.municipalWasteFee = municipalWasteFee;
        this.electionCommissionFee = electionCommissionFee;
        this.suspendReceipt = suspendReceipt;
        this.householdElectricityFee = householdElectricityFee;
        this.commonElectricityFee = commonElectricityFee;
        this.householdWaterFee = householdWaterFee;
        this.commonWaterFee = commonWaterFee;
        this.householdHotWaterFee = householdHotWaterFee;
        this.commonHotWaterFee = commonHotWaterFee;
        this.householdHeatingFee = householdHeatingFee;
        this.liftElectricityFee = liftElectricityFee;
        this.tvLicenseFee = tvLicenseFee;
        this.sewageFee = sewageFee;
        this.fareCollectionDiscount = FareCollectionDiscountDto.builder()
                .maintenanceDiscount(maintenanceDiscount)
                .hiringDiscount(hiringDiscount)
                .summerElectricityDiscount(summerElectricityDiscount)
                .parkingFeeDiscount(parkingFeeDiscount)
                .voucherDiscount(voucherDiscount)
                .electricityDiscount(electricityDiscount)
                .waterDiscount(waterDiscount)
                .build();
    }
}
