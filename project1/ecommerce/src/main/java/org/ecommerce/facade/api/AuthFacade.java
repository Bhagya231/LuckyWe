package org.ecommerce.facade.api;

import org.ecommerce.dto.LoginRequest;
import org.ecommerce.dto.LoginResponse;
import org.ecommerce.dto.RegisterRequest;
import org.ecommerce.dto.RegisterResponse;

public interface AuthFacade
{
    RegisterResponse register(RegisterRequest request);

    LoginResponse login(LoginRequest request);
}
