package com.yash.finance.dto.requestDTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthDTO {
    String email;
    String pass;
}
