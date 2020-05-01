package com.authservice.authservice.dao;

import com.authservice.authservice.exception.DependencyFailedException;
import com.authservice.authservice.exception.NotFoundException;
import com.authservice.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

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

            String url = hostname + path;

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("userName", userName);

            ResponseEntity responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    null,
                    Object[].class
            );

            userObjects = restTemplate.getForObject(hostname + path + "?" + "userName=" + userName, Object[].class);

            List<User> users = null;
            if (userObjects != null) {
                users = Arrays.asList(userObjects).stream().map(s -> {
                    LinkedHashMap response = (LinkedHashMap) s;
                    checkNullOrEmpty(response.get("id"));
                    checkNullOrEmpty(response.get("firstName"));
                    checkNullOrEmpty(response.get("lastName"));
                    checkNullOrEmpty(response.get("userName"));
                    checkNullOrEmpty(response.get("password"));
                    User userMapped = new User(response.get("id").toString(),
                            response.get("firstName").toString(), response.get("lastName").toString(),
                            response.get("userName").toString(), response.get("password").toString());
                    return userMapped;
                }).collect(Collectors.toList());
            }
            return users;
        } catch (Exception e) {
            if (((HttpClientErrorException) e).getStatusCode().value() == 400)
                throw new NotFoundException("User Not found with userName = " + userName);
            else
                throw new DependencyFailedException("Exception = " + e.getMessage());
        }
    }

    void checkNullOrEmpty(Object key) {
        if (key == null || (key instanceof String && ((String) key).trim().isBlank()))
            throw new DependencyFailedException(key + " is either null or empty");
    }
}
