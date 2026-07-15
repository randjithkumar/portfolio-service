package com.peplatform.portfolioservice.integration.marketdata.provider;

import com.peplatform.portfolioservice.integration.marketdata.dto.MarketPriceResponse;
import com.peplatform.portfolioservice.integration.marketdata.exception.MarketDataProviderException;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;
import tools.jackson.databind.JsonNode;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Locale;

@Component
public class YahooMarketDataProvider implements MarketDataProvider {

    private static final String PROVIDER = "YAHOO_FINANCE";
    private static final String RESILIENCE_INSTANCE = "yahooMarketData";

    private final RestClient yahooMarketDataRestClient;

    public YahooMarketDataProvider(
            @Qualifier("yahooMarketDataRestClient")
            RestClient yahooMarketDataRestClient
    ) {
        this.yahooMarketDataRestClient = yahooMarketDataRestClient;
    }

    @Override
    @Retry(name = RESILIENCE_INSTANCE)
    @CircuitBreaker(name = RESILIENCE_INSTANCE)
    public MarketPriceResponse getLatestPrice(String symbol) {
        try {
            JsonNode response = yahooMarketDataRestClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/v8/finance/chart/{symbol}")
                            .queryParam("interval", "1d")
                            .queryParam("range", "1d")
                            .build(symbol))
                    .retrieve()
                    .onStatus(
                            HttpStatusCode::is4xxClientError,
                            (request, response1) -> {
                                throw new MarketDataProviderException(
                                        PROVIDER,
                                        "Yahoo rejected the request with status "
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
                                        "Yahoo server error: "
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
                    "Yahoo HTTP request failed with status "
                            + exception.getStatusCode(),
                    exception.getStatusCode().is5xxServerError(),
                    exception
            );

        } catch (Exception exception) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Unable to retrieve price from Yahoo: "
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
        if (response == null || response.isNull()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Yahoo returned an empty response",
                    true
            );
        }

        JsonNode chart = response.path("chart");

        if (chart.isMissingNode() || chart.isNull()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Unexpected Yahoo response format: chart is missing",
                    true
            );
        }

        JsonNode error = chart.get("error");

        if (error != null && !error.isNull()) {
            String description = error.path("description")
                    .asText("Yahoo returned an error");

            throw new MarketDataProviderException(
                    PROVIDER,
                    description,
                    false
            );
        }

        JsonNode result = chart.path("result");

        if (!result.isArray() || result.isEmpty()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Symbol not found in Yahoo: " + requestedSymbol,
                    false
            );
        }

        JsonNode firstResult = result.get(0);
        JsonNode meta = firstResult.path("meta");

        if (meta.isMissingNode() || meta.isNull()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Yahoo response does not contain meta information",
                    true
            );
        }

        JsonNode priceNode = meta.get("regularMarketPrice");

        if (priceNode == null || priceNode.isNull()) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Yahoo response does not contain regularMarketPrice",
                    true
            );
        }

        BigDecimal price = priceNode.decimalValue();

        if (price.signum() < 0) {
            throw new MarketDataProviderException(
                    PROVIDER,
                    "Yahoo returned an invalid negative price",
                    true
            );
        }

        String returnedSymbol = meta.path("symbol")
                .asText(requestedSymbol)
                .trim()
                .toUpperCase(Locale.ROOT);

        String currency = meta.path("currency")
                .asText("USD")
                .trim()
                .toUpperCase(Locale.ROOT);

        return new MarketPriceResponse(
                returnedSymbol,
                price,
                currency,
                extractTimestamp(firstResult, meta),
                PROVIDER,
                true
        );
    }

    private Instant extractTimestamp(
            JsonNode result,
            JsonNode meta
    ) {
        JsonNode regularMarketTime = meta.get("regularMarketTime");

        if (regularMarketTime != null
                && !regularMarketTime.isNull()
                && regularMarketTime.canConvertToLong()
                && regularMarketTime.asLong() > 0) {
            return Instant.ofEpochSecond(
                    regularMarketTime.asLong()
            );
        }

        JsonNode timestamps = result.path("timestamp");

        if (timestamps.isArray() && !timestamps.isEmpty()) {
            JsonNode latest = timestamps.get(timestamps.size() - 1);

            if (latest != null
                    && latest.canConvertToLong()
                    && latest.asLong() > 0) {
                return Instant.ofEpochSecond(latest.asLong());
            }
        }

        return Instant.now();
    }

    @Override
    public String getProviderName() {
        return PROVIDER;
    }
}