package com.yash.finance.dto.requestDTOs;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CreateUserDTO {
    String name;
    String email;
    String password;
    String role;
    String status;
}
