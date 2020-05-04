package com.authservice.authservice.controller;

import com.authservice.authservice.config.JwtTokenUtil;
import com.authservice.authservice.model.AuthenticatedResponse;
import com.authservice.authservice.model.JWTRequest;
import com.authservice.authservice.model.JWTResponse;
import com.authservice.authservice.model.User;
import com.authservice.authservice.service.UserDetailsServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
/*
 * For supporting CORS @CrossOrigin is supported by SpringBoot and by
 * default @CrossOrigin allows all origin and HTTP methods specified
 */
@CrossOrigin
@Slf4j
@RequestMapping("/v1")
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsServiceImpl userDetailsServiceImpl;

    public AuthenticationController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, UserDetailsServiceImpl userDetailsServiceImpl) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    /**
     * This function is generating authentication token if user credentials are valid
     *
     * @param authenticationRequest
     * @return
     * @throws Exception
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JWTRequest authenticationRequest) throws Exception {
        log.info("Entering AuthenticationController.createAuthenticationToken with Parameter authenticationRequest {}.", authenticationRequest.toString());
        authenticate(authenticationRequest.getUserName(), authenticationRequest.getPassword());
        final UserDetails userDetails = userDetailsServiceImpl.loadUserByUsername(authenticationRequest.getUserName());
        final String token = jwtTokenUtil.generateToken((User) userDetails);
        return ResponseEntity.ok(new JWTResponse(token));
    }

    /**
     * This function will validate the token and return the userId
     *
     * @return
     */
    @GetMapping("/validating-token")
    public ResponseEntity<AuthenticatedResponse> validatingToken(Authentication auth) {
        User currentUser = (User) auth.getPrincipal();
        String loggedInUserId = User.loggedInUserIdBuilder().loggedInUserId(currentUser.getId()).build();
        return ResponseEntity.ok().body(new AuthenticatedResponse(loggedInUserId));
    }

    /**
     * It will authenticate the request with userName and passsword
     *
     * @param userName
     * @param password
     * @throws Exception
     */
    private void authenticate(String userName, String password) throws Exception {
        log.info("Entering AuthenticationController.authenticate with parameters userName {}", userName);
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
    }
}
