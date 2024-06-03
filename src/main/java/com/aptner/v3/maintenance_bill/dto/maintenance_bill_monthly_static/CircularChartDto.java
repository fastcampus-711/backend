package com.aptner.v3.maintenance_bill.dto.maintenance_bill_monthly_static;

import jakarta.persistence.Embeddable;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CircularChartDto {
    private String rankFirstColumnName;
    private int rankFirstColumnValue;
    private String rankSecondColumnName;
    private int rankSecondColumnValue;
    private String rankThirdColumnName;
    private int rankThirdColumnValue;
    private String rankFourthColumnName;
    private int rankFourthColumnValue;
    private String rankFifthColumnName;
    private int rankFifthColumnValue;
    private String etc;
    private int etcValue;
}
