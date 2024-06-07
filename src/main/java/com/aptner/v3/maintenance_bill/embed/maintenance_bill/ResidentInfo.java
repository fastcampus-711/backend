package com.aptner.v3.maintenance_bill.embed.maintenance_bill;

import com.aptner.v3.maintenance_bill.domain.House;
import com.aptner.v3.maintenance_bill.domain.type.ResidentType;
import com.aptner.v3.maintenance_bill.domain.type.residenceType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Embeddable
public class ResidentInfo {
    private String name;

    @OneToOne
    private House house;

    @Enumerated(EnumType.STRING)
    private ResidentType type = ResidentType.RESIDENT;

    private LocalDate occupancyDate;

    @Enumerated(EnumType.STRING)
    private residenceType residenceType;

    public ResidentInfo(House house) {
        this.house = house;
    }
}
