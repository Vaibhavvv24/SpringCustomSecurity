package com.example.springsecurity.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class Websec {

    private static final String[] WHITE_LIST_URLS = {
            "/hello",
            "/register",
            "/verifyRegistration*",
            "/resendVerificationToken*",
            "/resetpwd*",
            "/savepwd*"
    };
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(10);
    }
@Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{
//        http.authorizeHttpRequests(configurer->
//                configurer.requestMatchers(WHITE_LIST_URLS).permitAll()
//        );
    http
            .cors()
            .and()
            .csrf()
            .disable()
            .authorizeHttpRequests()
            .requestMatchers(WHITE_LIST_URLS).permitAll();
        //use http basic auth
        //http.httpBasic(Customizer.withDefaults());

        //disable csrf(Cross site request forgery)
//        http.csrf(csrf->csrf.disable());
//        http.cors(cors->cors.disable());

        return http.build();
    }
}
