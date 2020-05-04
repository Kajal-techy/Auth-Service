package com.authservice.authservice.config;

import com.authservice.authservice.exception.Forbidden;
import com.authservice.authservice.service.UserDetailsServiceImpl;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class AuthRequestFilter extends OncePerRequestFilter {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthRequestFilter(UserDetailsServiceImpl userDetailsServiceImpl, JwtTokenUtil jwtTokenUtil ) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    /**
     * This function authenticate the request after request is intercepted by spring security interceptor
     *
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     * @throws IllegalArgumentException
     * @throws ExpiredJwtException
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException, IllegalArgumentException, ExpiredJwtException {
        log.info("Entering AuthRequestFilter.doFilterInternal with parameters request {}, response {}, chain {}", request, response, chain);

        final String requestTokenHeader = request.getHeader("Authorization");

        String username = null;
        String jwtToken = null;

        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            jwtToken = requestTokenHeader.substring(7);
            username = jwtTokenUtil.getUsernameFromToken(jwtToken);

        } else
            throw new Forbidden("Token is not starting with Bearer");

        /* Once we get the token validate it. */
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsServiceImpl.loadUserByUsername(username);

            if (jwtTokenUtil.validateToken(jwtToken, userDetails)) {

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        chain.doFilter(request, response);
    }
}
