package com.aptner.v3.maintenance_bill;

import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@RestController
@RequestMapping("/maintenance-bill")
@RequiredArgsConstructor
public class MaintenanceBillController {
    private final MaintenanceBillBatchService maintenanceBillBatchService;
    private final MaintenanceBillService maintenanceBillService;

    @GetMapping("/test")
    public ApiResponse<?> getAllMaintenanceBills() {
        maintenanceBillBatchService.makeMaintenanceBillMonthlyStatistics(LocalDate.of(2024, 5, 1));

        return null;
    }

    @Deprecated
    @PostMapping("/upload")
    public ApiResponse<?> uploadMaintenanceBills(@RequestBody MultipartFile file) {
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
