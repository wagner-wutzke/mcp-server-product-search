package net.wowdev.ai.mcp.server.product.search.service;

import com.firecrawl.client.FirecrawlClient;
import com.firecrawl.models.SearchData;
import com.firecrawl.models.SearchOptions;
import org.springframework.stereotype.Service;

@Service
public class ProductSearchService {

    private final FirecrawlClient firecrawlClient;

    private final static int limit = 30;

    public ProductSearchService(FirecrawlClient firecrawlClient) {
        this.firecrawlClient = firecrawlClient;
    }

    public SearchData searchWeb(String query) {
        return firecrawlClient.search(query, SearchOptions.builder()
                .limit(limit)
                .build());
    }
}
