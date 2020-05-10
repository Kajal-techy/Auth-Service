package com.authservice.authservice.config;

import com.authservice.authservice.model.AuthenticationFailureResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    /**
     * It is use to throw the error message and handle exception if provided JWT token is not valid
     *
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws IllegalArgumentException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException)
            throws IOException, IllegalArgumentException {
        log.info("Entering JwtAuthenticationEntryPoint.commence with parameters request {}, response {}, authException {} ", request, response, authException);
        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        AuthenticationFailureResponse auth = new AuthenticationFailureResponse(authException.getMessage(), 401);
        ObjectMapper mapper = new ObjectMapper();
        response.getOutputStream().println(mapper.writeValueAsString(auth));
    }
}
