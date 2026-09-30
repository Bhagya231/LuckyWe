package org.ecommerce.security;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter  // Spring provides a filter designed to execute once for a request dispatch
{
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,  FilterChain filterChain) throws ServletException, IOException
    {

        //  Look for the "Authorization" header in the incoming request
        final String authHeader = request.getHeader("Authorization");


        //  If the header is missing or doesn't start with "Bearer ", pass it down the chain.
        // (If the endpoint is secured, Spring Security will block it later. If public, it allows it).
        if (authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        //  Extract the token (removing the "Bearer " prefix) and the email
        String jwt = authHeader.substring(7);
        String userEmail = jwtUtils.extractEmail(jwt);

        //  If we found an email, and the user isn't already authenticated in this request cycle
        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null)
        {

            // Fetch  user details from the database
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

            //  Check if the token is valid for this specific user
            if (jwtUtils.isTokenValid(jwt, userDetails))
            {

                //  Create an authentication object containing the user and their roles
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                //  Save the authentication object in the SecurityContext.
                // Now Spring knows exactly who is logged in and what roles they have.
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the request to the next filter or the controller
        filterChain.doFilter(request, response);
    }
}
