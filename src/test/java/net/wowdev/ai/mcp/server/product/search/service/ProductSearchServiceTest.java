package net.wowdev.ai.mcp.server.product.search.service;

import com.firecrawl.client.FirecrawlClient;
import com.firecrawl.models.SearchData;
import com.firecrawl.models.SearchOptions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ProductSearchServiceTest {
    @Test
    void searchesWithQueryAndThirtyResultLimit() {
        FirecrawlClient client = mock(FirecrawlClient.class);
        SearchData expected = new SearchData();
        when(client.search(eq("laptop"), argThat(options -> options.getLimit() == 30))).thenReturn(expected);
        ProductSearchService service = new ProductSearchService(client);

        assertSame(expected, service.searchWeb("laptop"));
        verify(client).search(eq("laptop"), argThat(options -> options.getLimit() == 30));
    }
}
