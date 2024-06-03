package com.aptner.v3.maintenance_bill.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Embeddable
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HouseAverageMaintenanceFeePrimaryKeys {
    private int houseCode;
    private double squareMeter;
    private LocalDate targetDate;
}
