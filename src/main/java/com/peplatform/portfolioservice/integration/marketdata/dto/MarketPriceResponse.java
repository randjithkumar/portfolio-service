package com.peplatform.portfolioservice.integration.marketdata.dto;

import java.math.BigDecimal;
import java.time.Instant;

/**
 * A data class representing a response from the market price service.
 * It encapsulates key pieces of information such as the ticker symbol, current price,
 * currency, timestamp of the data, source, and whether to fall back to a legacy mode if needed.
 * <p>
 * The main usage lies within the services layer where this model is used to transmit
 * market data between different parts of the system. Its design ensures that it follows best practices
 * in object-oriented design by avoiding infrastructure concerns and focusing on clear and concise information.
 *
 * @author Randjith
 * @version 1.0.0
 * @email randjithkumar@no-reply.github.com
 * @date 2026.07.15
 * @since 0.0.1-SNAPSHOT
 */
public record MarketPriceResponse(
        String symbol,
        BigDecimal price,
        String currency,
        Instant asOf,
        String source,
        boolean fallback
) {
}
