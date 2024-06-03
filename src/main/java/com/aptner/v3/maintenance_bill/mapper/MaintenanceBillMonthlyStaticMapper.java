package com.aptner.v3.maintenance_bill.mapper;

import com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static.*;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill_monthly_static.*;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface MaintenanceBillMonthlyStaticMapper {
    MaintenanceBillMonthlyStaticMapper INSTANCE = Mappers.getMapper(MaintenanceBillMonthlyStaticMapper.class);

    CircularChartDto maintenanceBillMonthlyStaticToCircularChartDto(CircularChart maintenanceBillMonthlyStatic);

    MaintenanceBillSummaryDto maintenanceBillMonthlyStaticToMaintenanceBillSummaryDto(MaintenanceBillSummary maintenanceBillSummary);

    MonthOnMonthInfoDto maintenanceBillMonthlyStaticToMonthOnMonthDto(MonthOnMonthInfo monthOnMonthInfo);

    YearOnYearInfoDto maintenanceBillMonthlyStaticToYearOnYearDto(YearOnYearInfo yearOnYearInfo);

    SquareOnSquareInfoDto maintenanceBillMonthlyStaticToSquareOnSquareInfoDto(SquareOnSquareInfo squareOnSquareInfo);

    List<EnergyUsageDto> maintenanceBillMonthlyStaticToEnergyUsageDtos(List<EnergyUsage> energyUsages);
}
