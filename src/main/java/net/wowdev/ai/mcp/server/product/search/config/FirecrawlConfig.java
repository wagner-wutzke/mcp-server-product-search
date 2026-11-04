package net.wowdev.ai.mcp.server.product.search.config;

import com.firecrawl.client.FirecrawlClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FirecrawlConfig {

    @Bean
    public FirecrawlClient firecrawlClient(@Value("${firecrawl.api-key}") String apiKey) {
        return FirecrawlClient.builder()
                .apiKey(apiKey)
                .timeoutMs(30000)
                .maxRetries(3)
                .backoffFactor(2.0d)
                .build();
    }

}
