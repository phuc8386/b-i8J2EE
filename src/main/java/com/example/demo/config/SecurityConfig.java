package com.example.demo.config;

import com.example.demo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Autowired
    private AccountService accountService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(accountService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authenticationProvider(authenticationProvider())
                .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/login").permitAll()
                        .requestMatchers("/uploads/**", "/css/**", "/js/**", "/images/**").permitAll()
                        .requestMatchers("/books/add", "/books/save", "/books/edit/**", "/books/update/**", "/books/delete/**").hasRole("ADMIN")
                        .requestMatchers("/categories/**").hasRole("ADMIN")
                        .requestMatchers("/books", "/books/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/cart/**").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
            .formLogin(form -> form
                .loginPage("/login")
                .failureUrl("/login?error=true")
                .defaultSuccessUrl("/books", true)
                .permitAll())
                .logout(logout -> logout.logoutSuccessUrl("/login?logout").permitAll());

        return http.build();
    }
}
