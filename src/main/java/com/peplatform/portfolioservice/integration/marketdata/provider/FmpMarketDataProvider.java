package com.peplatform.portfolioservice.integration.marketdata.provider;

import com.peplatform.portfolioservice.integration.marketdata.config.MarketDataProperties;
import com.peplatform.portfolioservice.integration.marketdata.dto.MarketPriceResponse;
import com.peplatform.portfolioservice.integration.marketdata.exception.MarketDataProviderException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;

@Slf4j
@Component
public class FmpMarketDataProvider implements MarketDataProvider {

    private static final String PROVIDER = "FMP";
    private static final String RESILIENCE_INSTANCE = "fmpMarketData";

    private final RestClient fmpMarketDataRestClient;
    private final MarketDataProperties properties;

    public FmpMarketDataProvider(
            @Qualifier("fmpMarketDataRestClient")
            RestClient fmpMarketDataRestClient,
            MarketDataProperties properties
    ) {
        this.fmpMarketDataRestClient = fmpMarketDataRestClient;
        this.properties = properties;
    }

    @Override
    @Retry(name = RESILIENCE_INSTANCE)
    @CircuitBreaker(name = RESILIENCE_INSTANCE)
    public MarketPriceResponse getLatestPrice(String symbol) {
        validateApiKey();

        try {
            JsonNode response = fmpMarketDataRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/stable/quote")
                            .queryParam("symbol", symbol)
                            .queryParam(
                                    "apikey",
                                    properties.getFmp().getApiKey()
                            )
                            .build())
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            (request, response1) -> {
                                throw new MarketDataProviderException(
                                        PROVIDER,
                                        "FMP rejected the request with status "
                                                + response1.getStatusCode(),
                                        false
                                );
                            }
                    )
                    .onStatus(
                            HttpStatusCode::is5xxServerError,
                            (request, response1) -> {
                                throw new MarketDataProviderException(
                                        PROVIDER,
                                        "FMP server error: "
                                                + response1.getStatusCode(),
                                        true
                                );
                            }
                    )
                    .body(JsonNode.class);

            return mapResponse(response, symbol);

        } catch (MarketDataProviderException exception) {
            throw exception;

        } catch (RestClientResponseException exception) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "FMP HTTP request failed with status "
                            + exception.getStatusCode(),
                    exception.getStatusCode().is5xxServerError(),
                    exception
            );

        } catch (Exception exception) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Unable to retrieve price from FMP: "
                            + exception.getMessage(),
                    true,
                    exception
            );
        }
    }

    private MarketPriceResponse mapResponse(
            JsonNode response,
            String requestedSymbol
    ) {
        if (response == null || !response.isArray()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Unexpected FMP response format",
                    true
            );
        }

        if (response.isEmpty()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Symbol not found in FMP: " + requestedSymbol,
                    false
            );
        }

        JsonNode quote = response.get(0);
        JsonNode priceNode = quote.get("price");

        if (priceNode == null || priceNode.isNull()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "FMP response does not contain price",
                    true
            );
        }

        BigDecimal price = priceNode.decimalValue();

        if (price.signum() < 0) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "FMP returned an invalid negative price",
                    true
            );
        }

        String returnedSymbol = quote.path("symbol")
                .asText(requestedSymbol)
                .trim()
                .toUpperCase(Locale.ROOT);

        return new MarketPriceResponse(
                returnedSymbol,
                price,
                "USD",
                extractTimestamp(quote),
                PROVIDER,
                false
        );
    }

    private Instant extractTimestamp(JsonNode quote) {
        JsonNode timestampNode = quote.get("timestamp");

        if (timestampNode == null
                || timestampNode.isNull()
                || !timestampNode.canConvertToLong()
                || timestampNode.asLong() <= 0) {
            return Instant.now();
        }

        return Instant.ofEpochSecond(timestampNode.asLong());
    }

    private void validateApiKey() {
        String apiKey = properties.getFmp().getApiKey();

        if (apiKey == null || apiKey.isBlank()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "FMP API key is not configured",
                    false
            );
        }
    }

    @Override
    public String getProviderName() {
        return PROVIDER;
    }
}