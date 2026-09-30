package org.ecommerce.service.api;

import org.ecommerce.dto.LoginRequest;
import org.ecommerce.dto.LoginResponse;
import org.ecommerce.dto.RegisterRequest;
import org.ecommerce.dto.RegisterResponse;

public interface AuthService
{
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);


}
