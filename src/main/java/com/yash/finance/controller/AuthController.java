package com.yash.finance.controller;

import com.yash.finance.dto.requestDTOs.AuthDTO;
import com.yash.finance.entities.UserEntity;
import com.yash.finance.service.UserService;
import com.yash.finance.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.aspectj.bridge.MessageUtil.fail;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody(required = false) AuthDTO authDTO) throws Exception{
        try {
            if (authDTO == null) {
                throw new Exception("Request body missing");
            }

            String email = safeTrim(authDTO.getEmail());
            String password = safeTrim(authDTO.getPass());

            if (email.isEmpty() || password.isEmpty()) {
                throw new  Exception("Email/password required");
            }

            UserEntity user = userService.findByEmail(email);
            if (user == null) {
                throw new Exception("Invalid email or password");
            }

            String dbPassword = user.getPass();
            if (dbPassword == null || dbPassword.isBlank()) {
                throw new Exception("Invalid email or password");
            }

            if (!passwordEncoder.matches(password, dbPassword)) {
                throw new Exception("Invalid email or password");
            }

            String userType = safeTrim(user.getRole());
            if (userType.isEmpty()) userType = null;

            String token = jwtUtils.generateToken(email, userType);


            String name = safeTrim(user.getName());
            if (name.isEmpty()) name = null;


            return ResponseEntity.ok(token);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(fail("Something went wrong"));
        }
    }
    private static String safeTrim(String s) {
        return s == null ? "" : s.trim();
    }
}
