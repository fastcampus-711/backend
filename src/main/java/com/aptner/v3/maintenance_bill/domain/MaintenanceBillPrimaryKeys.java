package com.aptner.v3.maintenance_bill.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceBillPrimaryKeys implements Serializable {
    private long houseId;
    private LocalDate impositionDate;
}