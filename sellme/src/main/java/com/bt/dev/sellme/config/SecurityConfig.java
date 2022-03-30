package com.bt.dev.sellme.config;

import com.bt.dev.sellme.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Arrays;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return username -> userRepository.findByName(username)
                .map(user -> User.withDefaultPasswordEncoder()
                        .username(user.getName())
                        .password(user.getPassword())
                        .authorities(user.getRoles().toArray(new String[0]))
                        .build()).orElseThrow( ()-> new UsernameNotFoundException("No user named :" + username));
    }

    static final String USER = "USER";
    static final String INVENTORY = "INVENTORY";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests() //
                .mvcMatchers(HttpMethod.POST, "/*").hasRole(INVENTORY)
                .mvcMatchers(HttpMethod.DELETE, "/**").hasRole(INVENTORY) //
                .anyRequest().authenticated()
                .and() //
                .httpBasic()
                .and() //
                .formLogin()
                .and() //
                .csrf().disable();
    }

    static String role(String auth) {
        return "ROLE_" + auth;
    }

    @Bean
    CommandLineRunner userLoader(UserRepository repository) {
        return args -> {
            repository.save(new com.bt.dev.sellme.user.User("chriskabor", "secret123", Arrays.asList(role(USER))));
            repository.save(new com.bt.dev.sellme.user.User("admin", "admin123",Arrays.asList(role(USER), role(INVENTORY))));
        };
    }

}
