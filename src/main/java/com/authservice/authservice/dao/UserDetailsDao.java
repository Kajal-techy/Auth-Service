package com.authservice.authservice.dao;

import com.authservice.authservice.exception.NotFoundException;
import com.authservice.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Repository
public class UserDetailsDao {

    private RestTemplate restTemplate;

    @Value("${hostname.userservice}")
    private String hostname;

    @Value("${path.getUserByUserName}")
    private String path;

    UserDetailsDao(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * This function calls the getUserByUserName API which is in different microservice
     *
     * @param userName
     * @return User
     * @throws NotFoundException
     */
    public User[] getUserByUsername(String userName) throws NotFoundException {
        log.info("Entering UserDetailsDao.getUserByUsername with parameter userName {}.", userName);
        try {
            User[] user = restTemplate.getForObject(hostname + path + "?" +
                    "userName=" + userName, User[].class);
            return user;
        } catch (Exception e) {
            throw new NotFoundException("User Not Found : userName = " + userName);
        }
    }
}
