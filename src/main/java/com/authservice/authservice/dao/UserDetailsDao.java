package com.authservice.authservice.dao;

import com.authservice.authservice.exception.NotFoundException;
import com.authservice.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

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
    public List<User> getUserByUsername(String userName) throws NotFoundException {
        log.info("Entering UserDetailsDao.getUserByUsername with parameter userName {}.", userName);
        Object[] userObjects;
        try {
            userObjects = restTemplate.getForObject(hostname + path + "?" +
                    "userName=" + userName, Object[].class);

            String id = ((LinkedHashMap) userObjects[0]).get("id").toString();
            String firstName = ((LinkedHashMap) userObjects[0]).get("firstName").toString();
            String lastName = ((LinkedHashMap) userObjects[0]).get("lastName").toString();
            String username = ((LinkedHashMap) userObjects[0]).get("userName").toString();
            String password = ((LinkedHashMap) userObjects[0]).get("password").toString();

            List users = Arrays.asList(userObjects).stream().map(s -> new User(id, firstName, lastName, username, password)).collect(Collectors.toList());

            return users;

        } catch (Exception e) {
            throw new NotFoundException("User Not Found : userName = " + userName);
        }
    }
}
