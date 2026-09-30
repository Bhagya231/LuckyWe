package org.ecommerce.facade.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.LoginRequest;
import org.ecommerce.dto.LoginResponse;
import org.ecommerce.dto.RegisterRequest;
import org.ecommerce.dto.RegisterResponse;
import org.ecommerce.facade.api.AuthFacade;
import org.ecommerce.service.api.AuthService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthFacadeImpl implements AuthFacade
{
    private final AuthService authService;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        return authService.register(request);
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        return authService.login(request);
    }
}
