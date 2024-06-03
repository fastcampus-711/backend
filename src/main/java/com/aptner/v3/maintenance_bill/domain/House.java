package com.aptner.v3.maintenance_bill.domain;

import com.aptner.v3.maintenance_bill.domain.type.HouseType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class House {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int code;
    private String name;
    @Enumerated(value = EnumType.STRING)
    private HouseType houseType;
    private double squareMeter;
    private String dong;
    private String ho;

    @Builder
    public House(int code, String name, HouseType houseType, double squareMeter, String dong, String ho) {
        this.code = code;
        this.name = name;
        this.houseType = houseType;
        this.squareMeter = squareMeter;
        this.dong = dong;
        this.ho = ho;
    }
}
