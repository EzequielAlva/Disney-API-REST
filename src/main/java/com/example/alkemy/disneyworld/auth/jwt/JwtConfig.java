package com.example.alkemy.disneyworld.auth.jwt;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@NoArgsConstructor
@Component
@ConfigurationProperties(prefix = "application.jwt")
public class JwtConfig {

    private String prefix;
    private String authHeader;
    private Integer tokenExpirationAfterDays;

}
