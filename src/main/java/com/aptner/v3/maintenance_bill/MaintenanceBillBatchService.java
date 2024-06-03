package com.aptner.v3.maintenance_bill;

import com.aptner.v3.global.exception.custom.InValidHouseIdException;
import com.aptner.v3.global.exception.custom.UnreadableFileException;
import com.aptner.v3.maintenance_bill.domain.*;
import com.aptner.v3.maintenance_bill.domain.type.EnergyType;
import com.aptner.v3.maintenance_bill.domain.type.HouseType;
import com.aptner.v3.maintenance_bill.dto.maintenance_bill.*;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill_monthly_static.*;
import com.aptner.v3.maintenance_bill.mapper.HouseMapper;
import com.aptner.v3.maintenance_bill.mapper.MaintenanceBillMapper;
import com.aptner.v3.maintenance_bill.repository.HouseAverageMaintenanceFeeRepository;
import com.aptner.v3.maintenance_bill.repository.HouseRepository;
import com.aptner.v3.maintenance_bill.repository.MaintenanceBillMonthlyStaticRepository;
import com.aptner.v3.maintenance_bill.repository.MaintenanceBillRepository;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
public class MaintenanceBillBatchService {
    private final MaintenanceBillMapper maintenanceBillMapper = MaintenanceBillMapper.INSTANCE;
    private final HouseMapper houseMapper = HouseMapper.INSTANCE;

    private final HouseRepository houseRepository;
    private final MaintenanceBillRepository maintenanceBillRepository;
    private final MaintenanceBillMonthlyStaticRepository maintenanceBillMonthlyStaticRepository;
    private final HouseAverageMaintenanceFeeRepository houseAverageMaintenanceFeeRepository;


    public void uploadMaintenanceBills(MultipartFile file) {
        Workbook workbook;
        try {
            workbook = new XSSFWorkbook(file.getInputStream());
        } catch (IOException e) {
            throw new UnreadableFileException();
        }

        for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
            Sheet sheet = workbook.getSheetAt(i);
            if (!getA1CellValueAsString(sheet).equals("아파트명"))
                continue;

            Iterator<Row> rowIterator = sheet.iterator();
            List<String> headers = initializeHeaders(rowIterator);
            while (rowIterator.hasNext()) {
                Row row = rowIterator.next();
                if (row.getCell(0) == null || row.getCell(0).getStringCellValue().isEmpty())
                    break;

                saveMaintenanceBill(row, headers);
            }
        }
    }

    /**
     * @param targetDate monthlyStatistic 생성을 원하는 년월을 일로 입력 (format: yyyy-MM-dd)
     */
    public void makeMaintenanceBillMonthlyStatistics(LocalDate targetDate) {
        targetDate = LocalDate.of(targetDate.getYear(), targetDate.getMonth(), 1);

        List<MaintenanceBill> maintenanceBills = maintenanceBillRepository.findByImpositionDate(targetDate);

        for (MaintenanceBill maintenanceBill : maintenanceBills) {
            MaintenanceBillPrimaryKeys id = maintenanceBill.getId();

            CircularChart circularChart = makeCircularChart(maintenanceBill);

            MaintenanceBillSummary maintenanceBillSummary = makeMaintenanceBillSummary(maintenanceBill);

            MonthOnMonthInfo monthOnMonthInfo = makeMonthOnMonthInfo(targetDate, maintenanceBill);

            YearOnYearInfo yearOnYearInfo = makeYearOnYearInfo(targetDate, maintenanceBill);

            House house = houseRepository.findById(maintenanceBill.getId().getHouseId())
                    .orElseThrow(InValidHouseIdException::new);
            HouseAverageMaintenanceFee houseAverageMaintenanceFee = getHouseAverageMaintenanceFee(targetDate, house);
            SquareOnSquareInfo squareOnSquareInfo = makeSquareOnSquareInfo(houseAverageMaintenanceFee);

            List<EnergyUsage> energyUsages = getenergyUsages(maintenanceBill, house, houseAverageMaintenanceFee, targetDate);

            maintenanceBillMonthlyStaticRepository.save(
                    MaintenanceBillMonthlyStatic.builder()
                            .id(id)
                            .circularChart(circularChart)
                            .maintenanceBillSummary(maintenanceBillSummary)
                            .monthOnMonthInfo(monthOnMonthInfo)
                            .yearOnYearInfo(yearOnYearInfo)
                            .squareOnSquareInfo(squareOnSquareInfo)
                            .energyUsages(energyUsages)
                            .build()
            );
        }
    }

    private List<EnergyUsage> getenergyUsages(MaintenanceBill maintenanceBill,
                                              House house,
                                              HouseAverageMaintenanceFee houseAverageMaintenanceFee,
                                              LocalDate targetDate) {
        List<EnergyUsage> energyUsages = new ArrayList<>();

        MaintenanceBill lastYearMaintenanceBill = maintenanceBillRepository.findByHouseIdAndTargetDate(house.getId(), targetDate.minusYears(1))
                .orElse(null);

        energyUsages.add(EnergyUsage.builder()
                .energyType(EnergyType.ELECTRICITY)
                .presentUsage(maintenanceBill.getEnergyUsage().getElectricityUsage())
                .presentFee(maintenanceBill.getFareCollectionFee().getHouseholdElectricityFee())
                .lastYearUsage(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getEnergyUsage().getElectricityUsage() : 0)
                .lastYearFee(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getFareCollectionFee().getHouseholdElectricityFee() : 0)
                .averageUsageOfSameSquare(houseAverageMaintenanceFee.getAverageElectricityUsage())
                .averageFeeOfSameSquare(houseAverageMaintenanceFee.getAverageElectricityFee())
                .build());

        energyUsages.add(EnergyUsage.builder()
                .energyType(EnergyType.WATER)
                .presentUsage(maintenanceBill.getEnergyUsage().getWaterUsage())
                .presentFee(maintenanceBill.getFareCollectionFee().getHouseholdWaterFee())
                .lastYearUsage(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getEnergyUsage().getWaterUsage() : 0)
                .lastYearFee(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getFareCollectionFee().getHouseholdWaterFee() : 0)
                .averageUsageOfSameSquare(houseAverageMaintenanceFee.getAverageWaterUsage())
                .averageFeeOfSameSquare(houseAverageMaintenanceFee.getAverageWaterFee())
                .build());

        energyUsages.add(EnergyUsage.builder()
                .energyType(EnergyType.HOTWATER)
                .presentUsage(maintenanceBill.getEnergyUsage().getHotWaterUsage())
                .presentFee(maintenanceBill.getFareCollectionFee().getHouseholdHotWaterFee())
                .lastYearUsage(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getEnergyUsage().getHotWaterUsage() : 0)
                .lastYearFee(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getFareCollectionFee().getHouseholdHotWaterFee() : 0)
                .averageUsageOfSameSquare(houseAverageMaintenanceFee.getAverageHotWaterUsage())
                .averageFeeOfSameSquare(houseAverageMaintenanceFee.getAverageHotWaterFee())
                .build());

        energyUsages.add(EnergyUsage.builder()
                .energyType(EnergyType.HEATING)
                .presentUsage(maintenanceBill.getEnergyUsage().getHeatingUsage())
                .presentFee(maintenanceBill.getFareCollectionFee().getHouseholdHeatingFee())
                .lastYearUsage(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getEnergyUsage().getHeatingUsage() : 0)
                .lastYearFee(lastYearMaintenanceBill != null ? lastYearMaintenanceBill.getFareCollectionFee().getHouseholdHeatingFee() : 0)
                .averageUsageOfSameSquare(houseAverageMaintenanceFee.getAverageHeatingUsage())
                .averageFeeOfSameSquare(houseAverageMaintenanceFee.getAverageHeatingFee())
                .build());

        return energyUsages;
    }

    private SquareOnSquareInfo makeSquareOnSquareInfo(HouseAverageMaintenanceFee houseAverageMaintenanceFee) {
        return SquareOnSquareInfo.builder()
                .maxMaintenanceFeeOfSameSquares(houseAverageMaintenanceFee.getMaxMaintenanceFee())
                .averageMaintenanceFeeOfSameSquares(houseAverageMaintenanceFee.getAverageMaintenanceFee())
                .minMaintenanceFeeOfSameSquares(houseAverageMaintenanceFee.getMinMaintenanceFee())
                .build();
    }

    private HouseAverageMaintenanceFee getHouseAverageMaintenanceFee(LocalDate targetDate, House house) {


        return houseAverageMaintenanceFeeRepository.findByHouseCodeAndHouseSquareMeterAndTargetDate(house.getCode(), house.getSquareMeter(), targetDate)
                .orElse(
                        houseAverageMaintenanceFeeRepository.save(
                                maintenanceBillRepository.staticByHouseCodeAndHouseSquareMeterAndTargetDate(house.getCode(), house.getSquareMeter(), targetDate)
                        )
                );
    }

    private YearOnYearInfo makeYearOnYearInfo(LocalDate targetDate, MaintenanceBill maintenanceBill) {
        long maintenanceFeeOfLastYear = maintenanceBillRepository.getCurrentMonthFeeFindByHouseIdAndImpositionDate(maintenanceBill.getId().getHouseId(), targetDate.minusYears(1))
                .orElse(0L);

        return YearOnYearInfo.builder()
                .maintenanceFeeOfPresent(maintenanceBill.getCurrentMonthFee())
                .maintenanceFeeOfLastYear(maintenanceFeeOfLastYear)
                .build();
    }

    private MonthOnMonthInfo makeMonthOnMonthInfo(LocalDate targetDate, MaintenanceBill maintenanceBill) {
        long maintenanceFeeOfLastMonth = maintenanceBillRepository.getCurrentMonthFeeFindByHouseIdAndImpositionDate(maintenanceBill.getId().getHouseId(), targetDate.minusMonths(1))
                .orElse(0L);
        long maintenanceFeeOfTwoMonthsAgo = maintenanceBillRepository.getCurrentMonthFeeFindByHouseIdAndImpositionDate(maintenanceBill.getId().getHouseId(), targetDate.minusMonths(2))
                .orElse(0L);

        return MonthOnMonthInfo.builder()
                .maintenanceFeeOfPresent(maintenanceBill.getCurrentMonthFee())
                .maintenanceFeeOfLastMonth(maintenanceFeeOfLastMonth)
                .maintenanceFeeOfTwoMonthsAgo(maintenanceFeeOfTwoMonthsAgo)
                .build();
    }

    private MaintenanceBillSummary makeMaintenanceBillSummary(MaintenanceBill maintenanceBill) {
        return MaintenanceBillSummary.builder()
                .beforeDeadlineFee(maintenanceBill.getCurrentMonthFee())
                .fareCollectionDiscount(maintenanceBill.getFareCollectionFee().getFareCollectionDiscount())
                .unpaidFee(maintenanceBill.getUnpaidFee())
                .afterDeadlineFee(maintenanceBill.getAfterDeadlineFee())
                .build();
    }

    private CircularChart makeCircularChart(MaintenanceBill maintenanceBill) {
        Map<String, Integer> fees = maintenanceBill.getFees();

        List<String> keySet = new ArrayList<>(fees.keySet());
        keySet.sort((o1, o2) -> fees.get(o2).compareTo(fees.get(o1)));

        int etcValue = 0;
        for (int i = 5; i < keySet.size(); i++) {
            if (fees.get(keySet.get(i)) > 0)
                etcValue += fees.get(keySet.get(i));
        }

        return CircularChart.builder()
                .rankFirstColumnName(keySet.get(0))
                .rankFirstColumnValue(fees.get(keySet.get(0)))
                .rankSecondColumnName(keySet.get(1))
                .rankSecondColumnValue(fees.get(keySet.get(1)))
                .rankThirdColumnName(keySet.get(2))
                .rankThirdColumnValue(fees.get(keySet.get(2)))
                .rankFourthColumnName(keySet.get(3))
                .rankFourthColumnValue(fees.get(keySet.get(3)))
                .rankFifthColumnName(keySet.get(4))
                .rankFifthColumnValue(fees.get(keySet.get(4)))
                .etc("기타")
                .etcValue(etcValue)
                .build();
    }

    private void saveMaintenanceBill(Row row, List<String> headers) {
        maintenanceBillRepository.save(
                maintenanceBillMapper.maintenanceBillDtoToMaintenanceBill(buildMaintenanceBillDto(row, headers))
        );
    }

    private List<String> initializeHeaders(Iterator<Row> rowIterator) {
        List<String> headers = new ArrayList<>();
        rowIterator.next()
                .cellIterator()
                .forEachRemaining(cell -> headers.add(cell.getStringCellValue()));

        return headers;
    }

    private int getCellValueAsInt(Row row, List<String> headers, String headerName) {
        return (int) row.getCell(headers.indexOf(headerName)).getNumericCellValue();
    }

    private double getCellValueAsDouble(Row row, List<String> headers, String headerName) {
        return row.getCell(headers.indexOf(headerName)).getNumericCellValue();
    }

    private String getCellValueAsString(Row row, List<String> headers, String headerName) {
        Cell cell = row.getCell(headers.indexOf(headerName));
        if (cell.getCellType().equals(CellType.STRING)) {
            return cell.getStringCellValue();
        } else {
            return String.valueOf((int) cell.getNumericCellValue());
        }
    }

    private LocalDate getCellValueAsLocalDate(Row row, List<String> headers, String headerName) {
        Cell cell = row.getCell(headers.indexOf(headerName));
        if (cell.getCellType().equals(CellType.STRING))
            return LocalDate.parse(cell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        else
            return LocalDate.ofInstant(
                    row.getCell(headers.indexOf(headerName)).getDateCellValue().toInstant(),
                    ZoneId.systemDefault()
            );
    }

    private String getA1CellValueAsString(Sheet sheet) {
        return sheet.getRow(0).getCell(0).getStringCellValue();
    }

    private HouseType getHouseType(Row row, List<String> headers) {
        return Arrays.stream(HouseType.values())
                .filter(houseType -> houseType.getKorean().equals(getCellValueAsString(row, headers, "단지특성")))
                .findAny()
                .orElseThrow();
    }

    private long ifHouseInfoIsntPresentEnroll(Row row, List<String> headers) {
        int code = getCellValueAsInt(row, headers, "단지코드");
        String name = getCellValueAsString(row, headers, "아파트명");
        double squareMeter = getCellValueAsDouble(row, headers, "평형");
        String dong = getCellValueAsString(row, headers, "동");
        String ho = getCellValueAsString(row, headers, "호");

        Optional<House> optionalHouse = houseRepository.findByCodeAndDongAndHo(code, dong, ho);
        if (optionalHouse.isEmpty()) {
            HouseDto houseDto = HouseDto.builder()
                    .code(code)
                    .name(name)
                    .houseType(getHouseType(row, headers))
                    .squareMeter(squareMeter)
                    .dong(dong)
                    .ho(ho)
                    .build();

            return houseRepository.save(houseMapper.mapHouseDtoToHouse(houseDto)).getId();
        }
        return optionalHouse.get().getId();
    }

    private MaintenanceBillDto buildMaintenanceBillDto(Row row, List<String> headers) {
        long houseId = ifHouseInfoIsntPresentEnroll(row, headers);

        return MaintenanceBillDto.builder()
                .id(MaintenanceBillPrimaryKeysDto.builder()
                        .houseId(houseId)
                        .impositionDate(getCellValueAsLocalDate(row, headers, "부과년월"))
                        .build())
                .paymentDueDate(getCellValueAsLocalDate(row, headers, "관리비납부기한"))
                .startServiceDate(getCellValueAsLocalDate(row, headers, "사용기간시작일"))
                .endServiceDate(getCellValueAsLocalDate(row, headers, "사용기간종료일"))
                .beforeDeadlineFee(getCellValueAsInt(row, headers, "납기내"))
                .afterDeadlineFee(getCellValueAsInt(row, headers, "납기후"))
                .currentMonthFee(getCellValueAsInt(row, headers, "당월부과금액"))
                .subMaintenanceFee(SubMaintenanceFeeDto.builder()
                        .subMaintenanceTotalFee(getCellValueAsInt(row, headers, "관리비소계"))
                        .generalMaintenanceFee(getCellValueAsInt(row, headers, "일반관리비"))
                        .cleaningFee(getCellValueAsInt(row, headers, "청소비"))
                        .disIntectionFee(getCellValueAsInt(row, headers, "소독비"))
                        .liftFee(getCellValueAsInt(row, headers, "승강기유지비"))
                        .repairingFee(getCellValueAsInt(row, headers, "수선유지비"))
                        .longTermRepairingFee(getCellValueAsInt(row, headers, "장기수선충당금"))
                        .securityFee(getCellValueAsInt(row, headers, "경비비"))
                        .septictankFee(getCellValueAsInt(row, headers, "정화조청소"))
                        .insuranceFee(getCellValueAsInt(row, headers, "보험료"))
                        .representativeMeetingFee(getCellValueAsInt(row, headers, "대표회의비"))
                        .brokerageCommission(getCellValueAsInt(row, headers, "위탁수수료"))
                        .parkingFee(getCellValueAsInt(row, headers, "주차비"))
                        .commonHeatingFee(getCellValueAsInt(row, headers, "공동난방비"))
                        .build())
                .fareCollectionFee(FareCollectionFeeDto.builder()
                        .fareCollectionTotalFee(getCellValueAsInt(row, headers, "징수대행"))
                        .municipalWasteFee(getCellValueAsInt(row, headers, "생활폐기물수수료"))
                        .electionCommissionFee(getCellValueAsInt(row, headers, "선관위비용"))
                        .suspendReceipt(getCellValueAsInt(row, headers, "가수금"))
                        .householdElectricityFee(getCellValueAsInt(row, headers, "세대전기료"))
                        .commonElectricityFee(getCellValueAsInt(row, headers, "공동전기료"))
                        .householdWaterFee(getCellValueAsInt(row, headers, "세대수도료"))
                        .commonWaterFee(getCellValueAsInt(row, headers, "공동수도료"))
                        .householdHotWaterFee(getCellValueAsInt(row, headers, "세대온수비"))
                        .commonHotWaterFee(getCellValueAsInt(row, headers, "공동온수비"))
                        .householdHeatingFee(getCellValueAsInt(row, headers, "세대난방비"))
                        .liftElectricityFee(getCellValueAsInt(row, headers, "승강기전기"))
                        .tvLicenseFee(getCellValueAsInt(row, headers, "TV수신료"))
                        .sewageFee(getCellValueAsInt(row, headers, "하수도료"))
                        .fareCollectionDiscount(FareCollectionDiscountDto.builder()
                                .maintenanceDiscount(getCellValueAsInt(row, headers, "관리비차감"))
                                .hiringDiscount(getCellValueAsInt(row, headers, "일자리지원차감"))
                                .summerElectricityDiscount(getCellValueAsInt(row, headers, "전기하계할인"))
                                .parkingFeeDiscount(getCellValueAsInt(row, headers, "주차비차감"))
                                .voucherDiscount(getCellValueAsInt(row, headers, "바우처할인"))
                                .electricityDiscount(getCellValueAsInt(row, headers, "전기할인요금"))
                                .waterDiscount(getCellValueAsInt(row, headers, "수도할인요금"))
                                .build())
                        .build())
                .energyUsage(EnergyUsageDto.builder()
                        .electricityUsage(getCellValueAsInt(row, headers, "전기사용량"))
                        .waterUsage(getCellValueAsInt(row, headers, "수도사용량"))
                        .hotWaterUsage(getCellValueAsInt(row, headers, "온수사용량"))
                        .heatingUsage(getCellValueAsInt(row, headers, "난방사용량"))
                        .gasUsage(getCellValueAsInt(row, headers, "가스사용량"))
                        .previousMonthElectricityGuideline(getCellValueAsInt(row, headers, "전기전월지침"))
                        .currentMonthElectricityGuideline(getCellValueAsInt(row, headers, "전기당월지침"))
                        .previousMonthWaterGuideline(getCellValueAsInt(row, headers, "수도전월지침"))
                        .currentMonthWaterGuideline(getCellValueAsInt(row, headers, "수도당월지침"))
                        .previousMonthHotWaterGuideline(getCellValueAsInt(row, headers, "온수전월지침"))
                        .currentMonthHotWaterGuideline(getCellValueAsInt(row, headers, "온수당월지침"))
                        .previousMonthHeatingGuideline(getCellValueAsInt(row, headers, "난방전월지침"))
                        .currentMonthHeatingGuideline(getCellValueAsInt(row, headers, "난방당월지침"))
                        .previousMonthGasGuideline(getCellValueAsInt(row, headers, "가스전월지침"))
                        .currentMonthGasGuideline(getCellValueAsInt(row, headers, "가스당월지침"))
                        .build())
                .unpaidFee(getCellValueAsInt(row, headers, "미납금액"))
                .lateFee(getCellValueAsInt(row, headers, "미납연체"))
                .afterDeadlineLateFee(getCellValueAsInt(row, headers, "납기후연체료"))
                .build();
    }
}