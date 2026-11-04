package net.wowdev.ai.mcp.server.product.search.mcp;

import com.firecrawl.models.SearchData;
import net.wowdev.ai.mcp.server.product.search.service.ProductSearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.mcp.annotation.McpTool;
import org.springframework.ai.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ProductSearchMcpServer {

    private final static Logger logger = LoggerFactory.getLogger(ProductSearchMcpServer.class);

    private final ProductSearchService service;

    public ProductSearchMcpServer(ProductSearchService service) {
        this.service = service;
    }

    @McpTool(
            name = "search_products",
            description = "Search for product prices in the internet."
    )
    public String searchForProduct (
            @McpToolParam(description = "the product descriptions and attributes to searched for",
                    required = true) String query) {
        SearchData results = service.searchWeb(query);
        List<Map<String, Object>> pages = results == null ? null : results.getWeb();
        if (pages == null || pages.isEmpty()) {
            return "No pages found.";
        }

        String resultString = pages.stream()
                .map(page -> String.format("Title: %s\nURL: %s\nDescription: %s",
                        page.getOrDefault("title", ""),
                        page.getOrDefault("url", ""),
                        page.getOrDefault("description", "")))
                .collect(Collectors.joining("\n\n"));
        logger.debug(">> Result: {}", resultString);
        return resultString;
    }
}
