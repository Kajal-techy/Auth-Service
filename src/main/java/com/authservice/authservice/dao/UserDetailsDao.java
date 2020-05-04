package com.authservice.authservice.dao;

import com.authservice.authservice.exception.DependencyFailedException;
import com.authservice.authservice.exception.NotFoundException;
import com.authservice.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
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
        try {

            String url = hostname + path;

            UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(url)
                    .queryParam("userName", userName);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            HttpEntity<String> entity = new HttpEntity<String>(headers);

            ResponseEntity responseEntity = restTemplate.exchange(
                    uriBuilder.toUriString(),
                    HttpMethod.GET,
                    entity,
                    UserDto[].class
            );

            log.info("Response Entity" + responseEntity + "body = " + responseEntity.getBody().toString());

            ArrayList<User> users = null;

            if (responseEntity.getBody() != null) {

                UserDto[] userDtos = (UserDto[]) responseEntity.getBody();
                users = (ArrayList<User>) Arrays.stream(userDtos).map(response -> {
                    checkNullOrEmpty(response.getId());
                    checkNullOrEmpty(response.getFirstName());
                    checkNullOrEmpty(response.getLastName());
                    checkNullOrEmpty(response.getPassword());
                    checkNullOrEmpty(response.getUserName());
                    return new User(response.getId(), response.getFirstName(), response.getLastName(),
                            response.getUserName(), (response.getPassword()));
                }).collect(Collectors.toList());
            }
            return users;
        } catch (Exception exception) {
            if ((exception instanceof HttpClientErrorException) && ((HttpClientErrorException) exception).getStatusCode().value() == 400)
                throw new NotFoundException("User Not found with userName = " + userName);
            else
                throw new DependencyFailedException("Exception = " + exception.getMessage());
        }
    }

    void checkNullOrEmpty(Object key) {
        if (key == null || (key instanceof String && ((String) key).trim().isBlank()))
            throw new DependencyFailedException(key + " is either null or empty");
    }
}
