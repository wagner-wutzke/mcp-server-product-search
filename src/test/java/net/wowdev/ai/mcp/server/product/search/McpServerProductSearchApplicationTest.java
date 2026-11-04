package net.wowdev.ai.mcp.server.product.search;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;

import static org.mockito.Mockito.mockStatic;

class McpServerProductSearchApplicationTest {
    @Test
    void mainDelegatesToSpringApplication() {
        try (var springApplication = mockStatic(SpringApplication.class)) {
            McpServerProductSearchApplication.main(new String[]{"--spring.main.web-application-type=none"});
            springApplication.verify(() -> SpringApplication.run(
                    McpServerProductSearchApplication.class,
                    new String[]{"--spring.main.web-application-type=none"}));
        }
    }
}
