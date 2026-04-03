package com.yash.finance.dto.requestDTOs;

import lombok.*;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateFinancialDTO
{
    Long fId;
    Long amount=null;
    String type=null;
    String category=null;
    Date date=null;
    String description=null;
    Long userId=null;
}
