package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class SubMaintenanceFeeDto {
    private int subMaintenanceTotalFee;
    private int generalMaintenanceFee;
    private int cleaningFee;
    private int disIntectionFee;
    private int liftFee;
    private int repairingFee;
    private int longTermRepairingFee;
    private int securityFee;
    private int septictankFee;
    private int insuranceFee;
    private int representativeMeetingFee;
    private int brokerageCommission;
    private int parkingFee;
    private int commonHeatingFee;
}
