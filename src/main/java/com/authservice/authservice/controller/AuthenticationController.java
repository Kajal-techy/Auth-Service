package com.authservice.authservice.controller;

import com.authservice.authservice.config.JwtTokenUtil;
import com.authservice.authservice.model.JWTRequest;
import com.authservice.authservice.model.JWTResponse;
import com.authservice.authservice.service.UserDetailsServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
/*
 * For supporting CORS @CrossOrigin is supported by SpringBoot and by
 * default @CrossOrigin allows all origin and HTTP methods specified
 */
@CrossOrigin
@RequestMapping("/v1")
public class AuthenticationController {

    private static Logger logger = LoggerFactory.getLogger(AuthenticationController.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

    /**
     * This function is generating authentication token if user credentials are valid
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) throws Exception {
        logger.debug("Entering AuthenticationController.createAuthenticationToken with Parameter authenticationRequest {}.", authenticationRequest.toString());
        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JWTResponse(token));
    }

    /**
     * It will authenticate the request with userName and passsword
     * @param userName
     * @param password
     * @throws Exception
     */
    private void authenticate(String userName, String password) throws Exception {
        logger.debug("Entering AuthenticationController.authenticate with parameters userName {}", userName);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }
}
