package com.yash.finance.dto.requestDTOs;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FinancialRecordDTO
{
    Long amount;
    String type;
    String category;
    Date date;
    String description;
    Long userId;
}
