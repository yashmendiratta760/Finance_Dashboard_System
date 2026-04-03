package com.yash.finance.dto.requestDTOs;

import com.yash.finance.entities.FinanceRecordsEntity;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UpdateUserDTO {
    Long id;
    String name=null;
    String email=null;
    String pass=null;
    String role=null;
    String status = null;
    List<FinanceRecordsEntity> records = null;
}
