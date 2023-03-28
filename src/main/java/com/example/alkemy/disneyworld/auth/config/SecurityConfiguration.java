package com.example.alkemy.disneyworld.auth.config;

import com.example.alkemy.disneyworld.auth.jwt.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static com.example.alkemy.disneyworld.auth.user.Role.ADMIN;
import static com.example.alkemy.disneyworld.auth.user.Role.USER;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeHttpRequests()
                //Movie
                .requestMatchers(HttpMethod.POST, "/disney/movie").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/disney/movie/{id}").hasAnyRole(USER.name(), ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/disney/movie/{id}").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.PATCH, "/disney/movie/{id}").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/disney/all-movies").hasAnyRole(USER.name(), ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/disney/movies").hasAnyRole(USER.name(), ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/disney/movies/{idMovie}/characters/{idCharacter}").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/disney/movies/{idMovie}/characters/{idCharacter}").hasRole(ADMIN.name())
                //Character
                .requestMatchers(HttpMethod.POST, "/disney/character").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.GET, "/disney/character/{id}").hasAnyRole(USER.name(), ADMIN.name())
                .requestMatchers(HttpMethod.DELETE, "/disney/character/{id}").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.PATCH, "/disney/character/{id}").hasRole(ADMIN.name())
                .requestMatchers(HttpMethod.POST, "/disney/characters").hasAnyRole(USER.name(), ADMIN.name())
                //Genre
                .requestMatchers(HttpMethod.POST, "/disney/genre").hasRole(ADMIN.name())

                .requestMatchers("/disney/auth/**").permitAll()
                .requestMatchers("/swagger-ui/**").permitAll()
                .anyRequest()
                    .authenticated()
                .and()
                    .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
