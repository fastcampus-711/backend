package com.aptner.v3.maintenance_bill.mapper;

import com.aptner.v3.maintenance_bill.domain.MaintenanceBill;
import com.aptner.v3.maintenance_bill.dto.maintenance_bill.MaintenanceBillDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface MaintenanceBillMapper {
    MaintenanceBillMapper INSTANCE = Mappers.getMapper(MaintenanceBillMapper.class);

    MaintenanceBill maintenanceBillDtoToMaintenanceBill(MaintenanceBillDto maintenanceBillDto);

    MaintenanceBillDto maintenanceBillToMaintenanceBillDto(MaintenanceBill maintenanceBill);
}
