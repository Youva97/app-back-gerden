package com.examen.gerden.backend.security;

import com.examen.gerden.backend.services.impl.JwtFilterServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;

@Configuration
@EnableWebSecurity
public class ConfigurationSecurityApplication {
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final JwtFilterServiceImpl jwtFilterServiceImpl;
    private final UserDetailsService userDetailsService;

    public ConfigurationSecurityApplication(BCryptPasswordEncoder bCryptPasswordEncoder, JwtFilterServiceImpl jwtFilterServiceImpl, UserDetailsService userDetailsService) {
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.jwtFilterServiceImpl = jwtFilterServiceImpl;
        this.userDetailsService = userDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {

        return
                httpSecurity
                        .csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(
                                authorize ->
                                        authorize
                                                .requestMatchers(GET,"/utilisateurs/validation-email").permitAll()
                                                .requestMatchers(POST,"/utilisateurs/inscription").permitAll()
                                                .requestMatchers(POST,"/utilisateurs/connexion").permitAll()
                                                .requestMatchers(POST,"/utilisateurs/refresh-token").permitAll()
                                                .requestMatchers(POST,"/utilisateurs/modifier-mot-de-passe").permitAll()
                                                .requestMatchers(POST,"/utilisateurs/nouveau-mot-de-passe").permitAll()
                                                .anyRequest().authenticated()
                        )
                        .sessionManagement(httpSecuritySessionManagementConfigurer ->
                                httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                        )
                        .addFilterBefore(jwtFilterServiceImpl, UsernamePasswordAuthenticationFilter.class)
                        .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService){
        DaoAuthenticationProvider daoAuthenticationProvider= new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(bCryptPasswordEncoder);
        return daoAuthenticationProvider;
    }
}
