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
public class SubMaintenanceFee {
    private long subMaintenanceTotalFee;
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
