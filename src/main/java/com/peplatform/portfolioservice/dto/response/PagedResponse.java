package com.peplatform.portfolioservice.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * The {@link PagedResponse} class serves as a model for the pagination response returned by APIs. It encapsulates various fields such as success status, status code, message, timestamp, content (pages of results), total elements, total pages, current page number, element size, and boolean flags indicating whether it is the first or last page.
 *
 * @author Randjith
 * @className PagedResponse
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.11
 * @since 1.0.0
 */
@Getter
@Builder
public class PagedResponse<T> {

    /**
     * Indicates whether the response was successful.
     */
    private boolean success;

    /**
     * Status code indicating whether the request was successful or not.
     */
    private int status;

    /**
     * Response message
     */
    private String message;

    /**
     * Timestamp of the response, indicating when it was created.
     */
    private LocalDateTime timestamp;

    /**
     * List of elements returned in the response.
     * <p>The `content` field contains the actual data retrieved from the server, which is typically a list of objects defined by `T`.
     *
     * @see PagedResponse<T>
     */
    private List<T> content;

    /**
     * Total number of elements across all pages.
     */
    private long totalElements;

    /**
     * Total number of pages
     */
    private int totalPages;

    /**
     * The current page number.
     */
    private int page;

    /**
     * Number of elements per page
     */
    private int size;

    /**
     * Indicates whether this is the initial page.
     *
     */
    private boolean first;

    /**
     * Indicates whether the response was successful.
     */
    private boolean last;

}