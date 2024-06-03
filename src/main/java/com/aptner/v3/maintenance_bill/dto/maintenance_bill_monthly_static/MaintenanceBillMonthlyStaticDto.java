package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import com.aptner.v3.maintenance_bill.dto.maintenance_bill.MaintenanceBillPrimaryKeysDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
public class MaintenanceBillMonthlyStaticDto {
    private MaintenanceBillPrimaryKeysDto id;
    private CircularChartDto circularChart;
    private MaintenanceBillSummaryDto maintenanceBillSummary;
    private MonthOnMonthInfoDto monthOnMonthInfo;
    private YearOnYearInfoDto yearOnYearInfo;
    private SquareOnSquareInfoDto squareOnSquareInfo;
    private List<EnergyUsageDto> energyUsage;
}
