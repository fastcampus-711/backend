package com.aptner.v3.maintenance_bill;

import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/maintenance-bills")
@RequiredArgsConstructor
public class MaintenanceBillController {
    private final MaintenanceBillBatchService maintenanceBillBatchService;
    private final MaintenanceBillService maintenanceBillService;

    @GetMapping("/make-statistics")
    public ApiResponse<?> makeStatistics(@RequestParam("year") int year,
                                         @RequestParam("month") int month) {
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(year, month, 1));

        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2024, 6, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2024, 5, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2024, 4, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2024, 3, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2024, 2, 1));

        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2023, 6, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2023, 5, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2023, 4, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2023, 3, 1));
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2023, 2, 1));
        return null;
    }

    //    @Deprecated
    @PostMapping(value = "/upload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ApiResponse<?> uploadMaintenanceBills(@RequestPart(value = "file") MultipartFile file) {
        maintenanceBillBatchService.uploadMaintenanceBills(file);
        return ResponseUtil.ok(SuccessCode.UPLOAD_MAINTENANCE_BILLS_SUCCESS);
    }

    @GetMapping("/circular-chart")
    public ApiResponse<?> circularChart(@RequestParam("year") int year,
                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.circularChart(year, month));
    }

    @GetMapping("/summary")
    public ApiResponse<?> summary(@RequestParam("year") int year,
                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.summary(year, month));
    }

    @GetMapping("/month-on-month")
    public ApiResponse<?> monthOnMonth(@RequestParam("year") int year,
                                       @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.monthOnMonth(year, month));
    }

    @GetMapping("/year-on-year")
    public ApiResponse<?> yearOnYear(@RequestParam("year") int year,
                                     @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.yearOnYear(year, month));

    }

    @GetMapping("/square-on-square")
    public ApiResponse<?> squareOnSquare(@RequestParam("year") int year,
                                         @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.squareOnSquare(year, month));

    }

    @GetMapping("/energy-consumption-status")
    public ApiResponse<?> energyConsumptionStatus(@RequestParam("year") int year,
                                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.energyConsumptionStatus(year, month));
    }

    @GetMapping("/details")
    public ApiResponse<?> details(@RequestParam("year") int year,
                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.details(year, month));
    }
}
