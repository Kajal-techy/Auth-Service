package com.authservice.authservice.service;

import com.authservice.authservice.dao.UserDetailsDao;
import com.authservice.authservice.exception.NotFoundException;
import com.authservice.authservice.model.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class UserDetailsServiceImpl implements UserDetailsService {

    private UserDetailsDao userDetailsDao;

    public UserDetailsServiceImpl(UserDetailsDao userDetailsDao) {
        this.userDetailsDao = userDetailsDao;
    }

    /**
     * This function loads the userDetails
     *
     * @param userName
     * @return UserDetails
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        log.info("Entering UserDetailsServiceImpl.loadUserByUsername with parameter userName {}.", userName);
        User[] users = userDetailsDao.getUserByUsername(userName);
        if (users == null || users.length == 0)
            throw new UsernameNotFoundException("User not found with username: " + userName);
        return new org.springframework.security.core.userdetails.User(users[0].getUserName(), users[0].getPassword(), new ArrayList<>());
    }

    public User loadUserDetails(String userName) throws NotFoundException {
        log.info("Entering UserDetailsServiceImpl.loadUserDetails with parameter userName {}", userName);
        User[] users = userDetailsDao.getUserByUsername(userName);
        if (users == null || users.length == 0)
            throw new NotFoundException("User not found with username: " + userName);
        return users[0];
    }
}
