package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import com.aptner.v3.maintenance_bill.domain.type.HouseType;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class HouseDto {
    private int code;
    private String name;
    private HouseType houseType;
    private double squareMeter;
    private String dong;
    private String ho;
}
