package com.jansetu4.portal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jansetu4.jwt")
public class JwtConfig {

    private String secret;
    private long expirationMs;
}
