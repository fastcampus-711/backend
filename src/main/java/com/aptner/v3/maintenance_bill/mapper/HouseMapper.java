package com.aptner.v3.maintenance_bill.mapper;

import com.aptner.v3.maintenance_bill.domain.House;
import com.aptner.v3.maintenance_bill.dto.maintenance_bill.HouseDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface HouseMapper {
    HouseMapper INSTANCE = Mappers.getMapper(HouseMapper.class);

    House mapHouseDtoToHouse(HouseDto houseDto);
}
