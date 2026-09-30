package org.ecommerce.security;

import org.ecommerce.model.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


import java.util.Collection;
import java.util.List;



public class CustomUserDetails implements UserDetails
{
    // The wrapper holds the pure database entity
    private final User user;

    public CustomUserDetails(User user)
    {
        this.user = user;
    }

    // Expose the original user entity if we ever need it in our controllers
    public User getUser()
    {
        return user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return List.of( new SimpleGrantedAuthority("ROLE_" + user.getRole())); //Spring Security's authority
    }

    @Override
    public String getPassword()
    {
        return user.getPassword(); // prevent to write safer code i.e NPE

    }

    @Override
    public String getUsername()
    {
        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }
    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }
    @Override
    public boolean isEnabled()
    {
        return true;
    }

}
