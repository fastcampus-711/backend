package com.aptner.v3.maintenance_bill.domain;

import com.aptner.v3.maintenance_bill.embed.maintenance_bill.EnergyUsage;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill.FareCollectionFee;
import com.aptner.v3.maintenance_bill.embed.maintenance_bill.SubMaintenanceFee;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MaintenanceBill {
    @EmbeddedId
    private MaintenanceBillPrimaryKeys id;

    private LocalDate paymentDueDate;
    private LocalDate startServiceDate;
    private LocalDate endServiceDate;

    private long beforeDeadlineFee;
    private long afterDeadlineFee;
    private long currentMonthFee;
    @Embedded
    private SubMaintenanceFee subMaintenanceFee;
    @Embedded
    private FareCollectionFee fareCollectionFee;
    @Embedded
    private EnergyUsage energyUsage;

    private int unpaidFee;
    private int lateFee;
    private int afterDeadlineLateFee;

    public Map<String, Integer> getFees() {
        Map<String, Integer> rank = new HashMap<>();
        putFieldsToMap(this, rank, 0);
        return rank;
    }

    private void putFieldsToMap(Object clazz, Map<String, Integer> rank, int depth) {
        if (depth > 3)
            return;

        for (Field field : clazz.getClass().getDeclaredFields()) {
            String fieldTypeName = field.getType().getSimpleName();
            if (fieldTypeName.contains("HouseIdAndTargetDateAsPrimaryKey"))
                putFieldsToMap(this.getId(), rank, depth + 1);
            else if (fieldTypeName.contains("SubMaintenanceFee"))
                putFieldsToMap(this.getSubMaintenanceFee(), rank, depth + 1);
            else if (fieldTypeName.contains("FareCollectionFee"))
                putFieldsToMap(this.getFareCollectionFee(), rank, depth + 1);
            else if (fieldTypeName.contains("EnergyUsage"))
                putFieldsToMap(this.getEnergyUsage(), rank, depth + 1);
            else if (fieldTypeName.contains("FareCollectionDiscount"))
                putFieldsToMap(this.getFareCollectionFee().getFareCollectionDiscount(), rank, depth + 1);

            if (!field.getType().getSimpleName().equals("int"))
                continue;

            try {
                field.setAccessible(true);
                rank.put(field.getName(), field.getInt(clazz));
                field.setAccessible(false);
            } catch (Exception ignored) {
            }
        }
    }
}

