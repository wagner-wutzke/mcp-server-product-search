package net.wowdev.ai.mcp.server.product.search.config;

import com.firecrawl.client.FirecrawlClient;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class FirecrawlConfigTest {
    @Test
    void createsClientFromApiKey() {
        assertNotNull(new FirecrawlConfig().firecrawlClient("test-api-key"));
    }
}
