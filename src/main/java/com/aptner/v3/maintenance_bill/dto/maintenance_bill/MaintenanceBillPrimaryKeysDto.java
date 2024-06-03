package com.aptner.v3.maintenance_bill.dto.maintenance_bill;

import com.aptner.v3.maintenance_bill.excel_util.ExcelColumn;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class MaintenanceBillPrimaryKeysDto {
    private long houseId;
    @ExcelColumn(headerName = "부과년월")
    private LocalDate impositionDate;
}
