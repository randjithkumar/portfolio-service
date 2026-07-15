package com.peplatform.portfolioservice.integration.marketdata.exception;

public class MarketDataProviderException extends RuntimeException {

    private final String provider;
    private final boolean retryable;

    public MarketDataProviderException(String provider, String message, boolean retryable) {
        super(message);
        this.provider = provider;
        this.retryable = retryable;
    }

    public MarketDataProviderException(String provider, String message, boolean retryable, Throwable cause) {
        super(message, cause);
        this.provider = provider;
        this.retryable = retryable;
    }

    public String getProvider() {
        return provider;
    }

    public boolean isRetryable() {
        return retryable;
    }
}