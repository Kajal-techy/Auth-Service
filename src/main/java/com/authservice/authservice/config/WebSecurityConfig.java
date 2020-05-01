package com.authservice.authservice.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;

/* For creating one or ore new beans which need to be dealt at the runtime */
@Configuration
/* To enable the Spring security at the project level */
@EnableWebSecurity
/* For securing our methods with java configuration */
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthServiceBeanInitializers authServiceBeanInitializers;

    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    public WebSecurityConfig(UserDetailsService userDetailsService, JwtAuthenticationEntryPoint
            jwtAuthenticationEntryPoint, AuthServiceBeanInitializers authServiceBeanInitializers) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationEntryPoint = jwtAuthenticationEntryPoint;
        this.authServiceBeanInitializers = authServiceBeanInitializers;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(authServiceBeanInitializers.passwordEncoder());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    /**
     * It specifies which request is allowed without authentication
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        log.info("Entering WebSecurityConfig.configure with parameter httpSecurity {}.", httpSecurity);
        httpSecurity.csrf().disable().authorizeRequests()
                .antMatchers("/v1/authenticate").permitAll().anyRequest()
                .authenticated().and().exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }
}