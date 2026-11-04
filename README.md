# Product Search MCP Server

This project demonstrates how to expose a web search capability as a Model Context Protocol (MCP) tool using Spring AI
and the Firecrawl Java client. It is not a complete shopping or product-price comparison service.

> **Learning project:** This application was created for self-learning and experimental purposes. It is not
> production-ready accounting software or financial advice. Review the technical gaps below before using it with
> sensitive
> or important financial data.

## What it does

The server publishes an MCP tool named `search_products`. An MCP client supplies a product description or other search
query; the server asks Firecrawl to search the web and returns each result's title, URL, and description. If there are
no web results, it returns `No pages found.`

## Technology stack

- Java 21
- Spring Boot 4.1.2
- Spring AI 2.0.2 MCP server with the WebMVC/HTTP transport
- Firecrawl Java client 1.18.7
- Maven (the repository includes Maven Wrapper scripts)
- `springboot4-dotenv` for loading environment variables from a local `.env` file
- JUnit 5 and Mockito through Spring Boot's test starter

## Main components

| Component                           | Responsibility                                                                                                                        |
|-------------------------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `McpServerProductSearchApplication` | Starts the Spring Boot application.                                                                                                   |
| `FirecrawlConfig`                   | Creates the `FirecrawlClient` bean from `firecrawl.api-key`, setting a 30-second timeout, three retries, and a backoff factor of 2.0. |
| `ProductSearchService`              | Calls Firecrawl search with the supplied query and a limit of 30 results.                                                             |
| `ProductSearchMcpServer`            | Registers the `search_products` MCP tool, invokes the service, and formats web results as title, URL, and description.                |

## Firecrawl client flow

Spring injects `firecrawl.api-key` into `FirecrawlConfig`, which builds a reusable `FirecrawlClient`. When an MCP client
invokes `search_products`, `ProductSearchMcpServer` passes its required `query` argument to `ProductSearchService`. The
service calls `firecrawlClient.search(query, SearchOptions.builder().limit(30).build())`. The MCP component reads the
`web` results from the response and formats the fields. Missing title, URL, or description values are rendered as empty
strings; null or empty result lists produce `No pages found.`

An active Firecrawl API key and network access are required for live searches.

## Architecture

The application uses a simple layered, dependency-injected design:

```text
MCP client
    │ Streamable HTTP / synchronous MCP tool call
    ▼
ProductSearchMcpServer (search_products)
    ▼
ProductSearchService
    ▼
FirecrawlClient ── HTTPS search request ──► Firecrawl
    ▲                                         │
    └──────────── search results ◄────────────┘
```

Spring AI hosts the MCP server over WebMVC on the configured HTTP port. The MCP tool is synchronous. Firecrawl client
creation and credentials are centralized in configuration, while the MCP adapter and search service hold the request
handling and search behavior respectively.

## Configuration

The defaults are in `src/main/resources/application.properties`:

| Property                        | Default                     | Purpose                                                 |
|---------------------------------|-----------------------------|---------------------------------------------------------|
| `server.port`                   | `8081`                      | HTTP port for the server.                               |
| `spring.application.name`       | `mcp-server-product-search` | Spring application name.                                |
| `spring.ai.mcp.server.name`     | `Products Search`           | MCP server name.                                        |
| `spring.ai.mcp.server.version`  | `2.0.0`                     | MCP server version reported by the application.         |
| `spring.ai.mcp.server.protocol` | `STREAMABLE`                | MCP transport protocol.                                 |
| `spring.ai.mcp.server.type`     | `SYNC`                      | Synchronous MCP server mode.                            |
| `firecrawl.api-key`             | `${FIRECRAWL_API_KEY}`      | Firecrawl credential, provided through the environment. |

Set `FIRECRAWL_API_KEY` in the environment before starting the application. Alternatively, the dotenv integration can
load it from a `.env` file in the project directory. Keep credentials private and do not commit `.env` files.

Example `.env` entry:

```dotenv
FIRECRAWL_API_KEY=your_firecrawl_api_key
```

## Run

Requirements: Java 21 and network access to Firecrawl. From the project root, run:

```bash
export FIRECRAWL_API_KEY="your_firecrawl_api_key"
./mvnw spring-boot:run
```

On Windows, use `mvnw.cmd spring-boot:run` after setting `FIRECRAWL_API_KEY` in the shell. The server listens on port
`8081` by default. An MCP-compatible client can connect using Spring AI's Streamable HTTP MCP transport at the server's
configured address; client connection settings depend on the client being used.

To package the application as a jar:

```bash
./mvnw package
java -jar target/product-search-0.0.1-SNAPSHOT.jar
```

## Project gaps and out-of-scope work

This project is an educational experiment, so the following capabilities were not implemented because they are outside
its scope:

- Product-specific extraction, validation, or normalization; search results are general web pages.
- Price parsing, currency conversion, comparison, sorting, or availability checks.
- Product catalog, database, caching, or persistence.
- Additional search filters, pagination, or a configurable result limit (the limit is fixed at 30).
- Authentication or authorization for connecting MCP clients.
- A production deployment setup, monitoring, or operational health dashboards.
- A user interface or shopping cart / purchase workflow.

