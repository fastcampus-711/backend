package com.aptner.v3.maintenance_bill;

import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.error.response.ApiResponse;
import com.aptner.v3.global.util.ResponseUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Tag(name ="관리비")
@RestController
@RequestMapping("/maintenance-bills")
@RequiredArgsConstructor
public class MaintenanceBillController {
    private final MaintenanceBillBatchService maintenanceBillBatchService;
    private final MaintenanceBillService maintenanceBillService;

    @Operation(summary = "통계 자료 생성")
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

    @Operation(summary = "액셀 파일 업로드")
    @PostMapping("/upload")
    public ApiResponse<?> uploadMaintenanceBills(@RequestBody MultipartFile file) {
        maintenanceBillBatchService.uploadMaintenanceBills(file);

        return ResponseUtil.ok(SuccessCode.UPLOAD_MAINTENANCE_BILLS_SUCCESS);
    }

    @Operation(summary = "원형 도표 데이터")
    @GetMapping("/circular-chart")
    public ApiResponse<?> circularChart(@RequestParam("year") int year,
                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.circularChart(year, month));
    }

    @Operation(summary = "관리비 요약")
    @GetMapping("/summary")
    public ApiResponse<?> summary(@RequestParam("year") int year,
                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.summary(year, month));
    }

    @Operation(summary = "전월 대비 관리비 사용 금액 비교")
    @GetMapping("/month-on-month")
    public ApiResponse<?> monthOnMonth(@RequestParam("year") int year,
                                       @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.monthOnMonth(year, month));
    }

    @Operation(summary = "전년 동월과 당월 관리비 비교")
    @GetMapping("/year-on-year")
    public ApiResponse<?> yearOnYear(@RequestParam("year") int year,
                                     @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.yearOnYear(year, month));

    }

    @Operation(summary = "동일 면적 관리비 비교")
    @GetMapping("/square-on-square")
    public ApiResponse<?> squareOnSquare(@RequestParam("year") int year,
                                         @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.squareOnSquare(year, month));

    }

    @Operation(summary = "우리집 에너지 소비 현황")
    @GetMapping("/energy-consumption-status")
    public ApiResponse<?> energyConsumptionStatus(@RequestParam("year") int year,
                                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.energyConsumptionStatus(year, month));
    }

    @Operation(summary = "고지서 상세 내역")
    @GetMapping("/details")
    public ApiResponse<?> details(@RequestParam("year") int year,
                                  @RequestParam("month") int month) {
        return ResponseUtil.ok(maintenanceBillService.details(year, month));
    }
}
