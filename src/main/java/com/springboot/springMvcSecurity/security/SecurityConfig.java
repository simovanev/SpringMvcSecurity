package com.springboot.springMvcSecurity.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
public class SecurityConfig {
  @Bean
  public UserDetailsManager userDetailsManager(DataSource dataSource) {
      JdbcUserDetailsManager jdbcUserDetailsManager = new JdbcUserDetailsManager(dataSource);
      jdbcUserDetailsManager.setUsersByUsernameQuery(
              "select user_id,pw, active from members where user_id=?");
      jdbcUserDetailsManager.setAuthoritiesByUsernameQuery(
              "select user_id,role from roles where user_id=?");
      return jdbcUserDetailsManager;
  }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(config -> config
                        .requestMatchers("/").hasRole("EMPLOYEE")
                        .requestMatchers("/leaders/**").hasRole("MANAGER")
                        .requestMatchers("/system/**").hasRole("ADMIN")
                        .anyRequest().authenticated())

                .formLogin(form -> form
                        .loginPage("/loginPage")
                        .loginProcessingUrl("/loginProcessing")
                        .permitAll())
                .logout(LogoutConfigurer::permitAll)
                .exceptionHandling(config -> config.accessDeniedPage("/access-denied"));

        return http.build();
    }
     /* @Bean
    public InMemoryUserDetailsManager inMemoryUserDetailsManager() {
        UserDetails john = User.builder()
                .username("john")
                .password("{noop}test123")
                .roles("EMPLOYEE")
                .build();
        UserDetails mary = User.builder()
                .username("mary")
                .password("{noop}test123")
                .roles("EMPLOYEE", "MANAGER")
                .build();
        UserDetails susan = User.builder()
                .username("susan")
                .password("{noop}test123")
                .roles("MANAGER", "EMPLOYEE", "ADMIN")
                .build();

        return new InMemoryUserDetailsManager(susan, john, mary);
    }*/

}
