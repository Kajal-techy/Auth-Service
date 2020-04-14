package com.authservice.authservice.dao;

import com.authservice.authservice.exception.NotFoundException;
import com.authservice.authservice.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestTemplate;

@Repository
public class UserDetailsDao {

    private static Logger logger = LoggerFactory.getLogger(UserDetailsDao.class);

    private RestTemplate restTemplate;

    @Value("${hostname.getUserByUserName}")
    private String hostname;

    @Value("${path.getUserByUserName}")
    private String path;

    @Autowired
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
    public User getUserByUsername(String userName) throws NotFoundException {
        logger.debug("Entering UserDetailsDao.getUserByUsername with parameter userName {}.", userName);
        try {
            User user = restTemplate.getForObject(hostname + path + "?" +
                    "userName=" + userName, User.class);
            return user;
        } catch (Exception e) {
            throw new NotFoundException("User Not Found , Type : userName = " + userName);
        }
    }
}
