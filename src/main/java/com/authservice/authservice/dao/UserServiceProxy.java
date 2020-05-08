package com.authservice.authservice.dao;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("user-service")
public interface UserServiceProxy {
    @GetMapping("/v1/users")
    ResponseEntity<UserDto[]> getUserDetails(@RequestParam(value = "userName") String userName);
}


