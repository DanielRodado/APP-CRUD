package com.mindhub.AppCrud.configuration;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    /*@Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {

    }*/
}
