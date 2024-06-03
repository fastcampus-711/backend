package com.aptner.v3.maintenance_bill;

import com.aptner.v3.global.error.SuccessCode;
import com.aptner.v3.global.exception.custom.InValidUserInfoException;
import com.aptner.v3.global.util.MemberUtil;
import com.aptner.v3.maintenance_bill.domain.MaintenanceBill;
import com.aptner.v3.maintenance_bill.domain.MaintenanceBillMonthlyStatic;
import com.aptner.v3.maintenance_bill.dto.maintenance_bill.MaintenanceBillDto;
import com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static.*;
import com.aptner.v3.maintenance_bill.mapper.MaintenanceBillMapper;
import com.aptner.v3.maintenance_bill.mapper.MaintenanceBillMonthlyStaticMapper;
import com.aptner.v3.maintenance_bill.repository.MaintenanceBillMonthlyStaticRepository;
import com.aptner.v3.maintenance_bill.repository.MaintenanceBillRepository;
import com.aptner.v3.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MaintenanceBillService {
    private final MaintenanceBillMapper maintenanceBillMapper = MaintenanceBillMapper.INSTANCE;
    private final MaintenanceBillMonthlyStaticMapper maintenanceBillMonthlyStaticMapper = MaintenanceBillMonthlyStaticMapper.INSTANCE;

    private final MemberRepository memberRepository;
    private final MaintenanceBillRepository maintenanceBillRepository;
    private final MaintenanceBillMonthlyStaticRepository maintenanceBillMonthlyStaticRepository;


    public CircularChartDto circularChart(int year, int month) {
        MaintenanceBillMonthlyStatic maintenanceBillMonthlyStatic =
                getMaintenanceBillMonthlyStatic(year, month);

        return maintenanceBillMonthlyStaticMapper
                .maintenanceBillMonthlyStaticToCircularChartDto(
                        maintenanceBillMonthlyStatic.getCircularChart()
                );
    }

    public MaintenanceBillSummaryDto summary(int year, int month) {
        MaintenanceBillMonthlyStatic maintenanceBillMonthlyStatic =
                getMaintenanceBillMonthlyStatic(year, month);

        return maintenanceBillMonthlyStaticMapper
                .maintenanceBillMonthlyStaticToMaintenanceBillSummaryDto(
                        maintenanceBillMonthlyStatic.getMaintenanceBillSummary()
                );
    }

    public MonthOnMonthInfoDto monthOnMonth(int year, int month) {
        MaintenanceBillMonthlyStatic maintenanceBillMonthlyStatic =
                getMaintenanceBillMonthlyStatic(year, month);

        return maintenanceBillMonthlyStaticMapper
                .maintenanceBillMonthlyStaticToMonthOnMonthDto(
                        maintenanceBillMonthlyStatic.getMonthOnMonthInfo()
                );
    }

    public YearOnYearInfoDto yearOnYear(int year, int month) {
        MaintenanceBillMonthlyStatic maintenanceBillMonthlyStatic =
                getMaintenanceBillMonthlyStatic(year, month);

        return maintenanceBillMonthlyStaticMapper
                .maintenanceBillMonthlyStaticToYearOnYearDto(
                        maintenanceBillMonthlyStatic.getYearOnYearInfo()
                );
    }

    public SquareOnSquareInfoDto squareOnSquare(int year, int month) {
        MaintenanceBillMonthlyStatic maintenanceBillMonthlyStatic =
                getMaintenanceBillMonthlyStatic(year, month);

        return maintenanceBillMonthlyStaticMapper
                .maintenanceBillMonthlyStaticToSquareOnSquareInfoDto(
                        maintenanceBillMonthlyStatic.getSquareOnSquareInfo()
                );
    }

    public List<EnergyUsageDto> energyConsumptionStatus(int year, int month) {
        MaintenanceBillMonthlyStatic maintenanceBillMonthlyStatic =
                getMaintenanceBillMonthlyStatic(year, month);

        return maintenanceBillMonthlyStaticMapper
                .maintenanceBillMonthlyStaticToEnergyUsageDtos(
                        maintenanceBillMonthlyStatic.getEnergyUsages()
                );
    }

    public MaintenanceBillDto details(int year, int month) {
        LocalDate targetDate = LocalDate.of(year, month, 1);
        MaintenanceBill maintenanceBill = maintenanceBillRepository.findByHouseIdAndImpositionDate(getHouseId(), targetDate)
                .orElse(null);

        return maintenanceBillMapper.maintenanceBillToMaintenanceBillDto(maintenanceBill);
    }

    private MaintenanceBillMonthlyStatic getMaintenanceBillMonthlyStatic(int year, int month) {
        long houseId = getHouseId();

        LocalDate targetDate = LocalDate.of(year, month, 1);

        return maintenanceBillMonthlyStaticRepository.findByHouseIdAndTargetDate(houseId, targetDate)
                .orElseThrow();
    }

    private long getHouseId() {
        return memberRepository.findById(MemberUtil.getMemberId())
                .orElseThrow(InValidUserInfoException::new)
                .getResidentInfo()
                .getHouse()
                .getId();
    }
}
