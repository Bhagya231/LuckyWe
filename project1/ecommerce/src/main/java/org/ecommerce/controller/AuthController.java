package org.ecommerce.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.LoginRequest;
import org.ecommerce.dto.LoginResponse;
import org.ecommerce.dto.RegisterRequest;
import org.ecommerce.dto.RegisterResponse;
import org.ecommerce.facade.api.AuthFacade;
import org.ecommerce.utility.AppLogger;
import org.slf4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin
public class AuthController
{
    private static final Logger log = AppLogger.getLogger(AuthController.class);
    private final AuthFacade authFacade;


    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> register(@Valid @RequestBody RegisterRequest request)
    {
        log.info("Registration request received for email: {}", request.getEmail());
        RegisterResponse response = authFacade.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request)
    {
        log.info("Login request received for email: {}",request.getEmail());
        LoginResponse response = authFacade.login(request);


        return ResponseEntity.ok(response);
    }
}
