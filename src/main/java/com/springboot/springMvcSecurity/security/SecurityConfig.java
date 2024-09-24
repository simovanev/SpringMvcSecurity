package com.springboot.springMvcSecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails john = User.builder()
                .username("john")
                .password("{noop}test123")
                .roles("EMPLOYEE")
                .build();
        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}test123")
                .roles("EMPLOYEE","MANAGER")
                .build();
        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}test123")
                .roles("MANAGER", "EMPLOYEE", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(susan, john, mary);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(config -> config
                        .requestMatchers("/").hasRole("EMPLOYEE")
                        .requestMatchers("/leaders?**").hasRole("MANAGER")
                        .requestMatchers("/system/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/loginProcessing")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll);

        return http.build();
    }
}
