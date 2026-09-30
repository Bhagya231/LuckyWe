package org.ecommerce.service.impl;

import lombok.RequiredArgsConstructor;
import org.ecommerce.model.User;
import org.ecommerce.repository.UserRepository;
import org.ecommerce.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService
{
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->{return new UsernameNotFoundException("Username not found with email");});

        return new CustomUserDetails(user);
    }
}
