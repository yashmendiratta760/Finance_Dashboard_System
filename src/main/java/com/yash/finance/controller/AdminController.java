package com.yash.finance.controller;


import com.yash.finance.dto.requestDTOs.CreateUserDTO;
import com.yash.finance.dto.requestDTOs.FinancialRecordDTO;
import com.yash.finance.dto.requestDTOs.UpdateFinancialDTO;
import com.yash.finance.dto.requestDTOs.UpdateUserDTO;
import com.yash.finance.dto.requestDTOs.responseDTOs.ResponseDTO;
import com.yash.finance.entities.FinanceRecordsEntity;
import com.yash.finance.entities.UserEntity;
import com.yash.finance.service.FinancialRecordService;
import com.yash.finance.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController
{

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private FinancialRecordService financialRecordService;

    // USERS
    @PostMapping("/users")
    public ResponseEntity<?> createUser(
            @RequestBody CreateUserDTO createUserDTO
            )
    {
        try {
            if(createUserDTO.getName().isBlank() || createUserDTO.getEmail().isBlank()
            || createUserDTO.getPassword().isBlank() || createUserDTO.getRole().isBlank()) throw new Exception("Some field is empty");
            createUserDTO.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));
            UserEntity user = userService.createUser(createUserDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(e.getMessage()));
        }
    }
    // USERS
    @GetMapping("/users")
    public ResponseEntity<List<UserEntity>> getAllUsers()
    {
        try {
            return ResponseEntity.ok(userService.getAllUsers());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }
    // USERS
    @GetMapping("/users/{id}")
    public ResponseEntity<UserEntity> getUsersById(@PathVariable Long id)
    {
        try {
            return ResponseEntity.ok(userService.getById(id));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new UserEntity());
        }
    }
    // USERS
    @PutMapping("/users")
    public ResponseEntity<?> updateUser(
            @RequestBody UpdateUserDTO updateUserDTO
    )
    {
        try {
            UserEntity user = userService.updateUser(updateUserDTO);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(e.getMessage()));
        }
    }
    // USERS
    @PatchMapping("/user/status")
    public ResponseEntity<?> setUserStatus(@RequestBody UpdateUserDTO updateUserDTO){
        try {
            UserEntity user = userService.updateUser(updateUserDTO);
            return ResponseEntity.ok(user);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseDTO(e.getMessage()));
        }
    }

    //FINANCE
    @PostMapping("/records")
    public ResponseEntity<?> createFinancialRecord(
            @RequestBody FinancialRecordDTO financialRecordDTO
    ){
        try {
            FinanceRecordsEntity financeRecordsEntity =  financialRecordService.createEntry(financialRecordDTO);
            return ResponseEntity.ok(financeRecordsEntity);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
    //FINANCE
    @PutMapping("/records")
    public ResponseEntity<ResponseDTO> updateRecord(@RequestBody UpdateFinancialDTO updateFinancialDTO)
    {
        try {
            FinanceRecordsEntity financeRecordsEntity = financialRecordService.updateEntry(updateFinancialDTO);
            return ResponseEntity.ok(new ResponseDTO("record updated"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(e.getMessage()));
        }
    }

    @DeleteMapping("/records")
    public ResponseEntity<ResponseDTO> deleteRecord(@RequestParam Long id)
    {
        try {
            financialRecordService.deleteEntry(id);
            return ResponseEntity.ok(new ResponseDTO("record deleted"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseDTO(e.getMessage()));
        }
    }


}
