package com.jansetu4.portal.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "jansetu4.ai")
public class AiConfig {

    private String apiKey;
    private String model = "gpt-4o-mini";
    private String baseUrl = "https://api.openai.com/v1";
}
