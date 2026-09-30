package org.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.dto.LoginRequest;
import org.ecommerce.dto.LoginResponse;
import org.ecommerce.dto.RegisterRequest;
import org.ecommerce.dto.RegisterResponse;
import org.ecommerce.exception.EmailAlreadyExistsException;
import org.ecommerce.exception.InvalidCredentialsException;
import org.ecommerce.mapper.UserMapper;
import org.ecommerce.model.User;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.security.CustomUserDetails;
import org.ecommerce.security.JwtUtils;
import org.ecommerce.service.api.AuthService;
import org.ecommerce.utility.AppLogger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService
{
    private static final Logger log = AppLogger.getLogger(AuthServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;
    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    @Override
    public RegisterResponse register(RegisterRequest request) {

        log.info("Registration started for email: {}", request.getEmail());

        try {
            if (userRepository.existsByEmail(request.getEmail()) > 0)
            {
                log.warn("Registration failed. Email already exists: {}", request.getEmail());

                throw new EmailAlreadyExistsException("Email already exists");
            }

            User user = userMapper.toEntity(request);

            user.setPassword(passwordEncoder.encode(request.getPassword()));

            user.setRole("ROLE_CUSTOMER");
            User savedUser = userRepository.save(user);

            log.info("Customer registered successfully. User ID: {}", savedUser.getId());

            RegisterResponse response =  userMapper.toRegisterResponse(savedUser);
            response.setMessage("User registered successfully");

            return response;

        } catch (EmailAlreadyExistsException e) {

            throw e;

        } catch (Exception e) {

            log.error("Unexpected error during registration for email: {}", request.getEmail(), e);

            throw new RuntimeException("Unable to register user");
        }
    }

    @Override
    public LoginResponse login(LoginRequest request) {

        log.info("Login attempt for email: {}", request.getEmail());

        try {

            // This single line securely checks both the email and the hashed password.
            // If the password is wrong, it automatically throws a BadCredentialsException.
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> {

                        log.warn("Login failed. User not found: {}", request.getEmail());

                        return new InvalidCredentialsException("user not register with this email");});

//            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
//
//                log.warn("Login failed. Invalid password for email: {}", request.getEmail());
//
//                throw new InvalidCredentialsException("Invalid password try again");
//            }

//            log.info("Login successful. User ID: {}", user.getId());
//
//            LoginResponse response = userMapper.toLoginResponse(user);
//            response.setMessage("User Login successfully ");

            // Wrap the user, generate the token, and attach it to  mapped response.

            CustomUserDetails userDetails = new CustomUserDetails(user);
            String token = jwtUtils.generateToken(userDetails);

            LoginResponse response = userMapper.toLoginResponse(user);
            response.setMessage("User Login successfully ");
            response.setToken(token);


            return response;

        } catch (BadCredentialsException e) {
            // Catches the failed password check from the authenticationManager
            log.warn("Login failed. Invalid password for email: {}", request.getEmail());
            throw new InvalidCredentialsException("Invalid password try again");
        } catch (InvalidCredentialsException e) {

            throw e;

        } catch (Exception e) {

            log.error("Unexpected error during login for email: {}", request.getEmail(), e);

            throw new RuntimeException("Unable to login");
        }
    }


}
