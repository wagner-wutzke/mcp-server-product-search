package net.wowdev.ai.mcp.server.product.search.mcp;

import com.firecrawl.models.SearchData;
import net.wowdev.ai.mcp.server.product.search.service.ProductSearchService;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ProductSearchMcpServerTest {
    private final ProductSearchService service = mock(ProductSearchService.class);
    private final ProductSearchMcpServer server = new ProductSearchMcpServer(service);

    @Test
    void returnsMessageWhenSearchDataIsNull() {
        when(service.searchWeb("chair")).thenReturn(null);
        assertEquals("No pages found.", server.searchForProduct("chair"));
    }

    @Test
    void returnsMessageWhenWebResultsAreNullOrEmpty() {
        SearchData data = new SearchData();
        when(service.searchWeb("chair")).thenReturn(data);
        assertEquals("No pages found.", server.searchForProduct("chair"));

        data.setWeb(List.of());
        assertEquals("No pages found.", server.searchForProduct("chair"));
    }

    @Test
    void formatsAllPagesAndUsesEmptyValuesForMissingFields() {
        SearchData data = new SearchData();
        data.setWeb(List.of(
                Map.of("title", "Desk", "url", "https://shop.test/desk", "description", "Wood desk"),
                Map.of("title", "Lamp")));
        when(service.searchWeb("furniture")).thenReturn(data);

        assertEquals("Title: Desk\nURL: https://shop.test/desk\nDescription: Wood desk\n\n"
                        + "Title: Lamp\nURL: \nDescription: ",
                server.searchForProduct("furniture"));
    }
}
