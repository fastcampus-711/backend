package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import com.aptner.v3.maintenance_bill.excel_util.ExcelColumn;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MaintenanceBillDto {

    private MaintenanceBillPrimaryKeysDto id;

    @ExcelColumn(headerName = "관리비납부기한")
    private LocalDate paymentDueDate;
    @ExcelColumn(headerName = "사용기간시작일")
    private LocalDate startServiceDate;
    @ExcelColumn(headerName = "사용기간종료일")
    private LocalDate endServiceDate;

    private int beforeDeadlineFee;
    private int afterDeadlineFee;
    private int currentMonthFee;

    private SubMaintenanceFeeDto subMaintenanceFee;

    private FareCollectionFeeDto fareCollectionFee;

    private EnergyUsageDto energyUsage;

    private int unpaidFee;
    private int lateFee;
    private int afterDeadlineLateFee;
}
