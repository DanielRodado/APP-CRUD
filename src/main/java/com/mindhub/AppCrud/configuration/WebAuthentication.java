package com.mindhub.AppCrud.configuration;


import com.mindhub.AppCrud.models.Person;
import com.mindhub.AppCrud.models.subClass.Admin;
import com.mindhub.AppCrud.models.subClass.Teacher;
import com.mindhub.AppCrud.repositories.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebAuthentication extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userEmail -> {
            Person person = personRepository.findByEmail(userEmail);
            if (person != null) {
                return new User(person.getEmail(), person.getPassword(), AuthorityUtils.createAuthorityList(
                        person instanceof Admin ? "ADMIN" : person instanceof Teacher ? "TEACHER" : "STUDENT"
                ));
            } else {
                throw new UsernameNotFoundException("Unknown user: " + userEmail);
            }
        });
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}
