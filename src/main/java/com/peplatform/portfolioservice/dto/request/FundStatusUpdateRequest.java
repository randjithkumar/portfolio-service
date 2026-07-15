package com.peplatform.portfolioservice.dto.request;

import com.peplatform.portfolioservice.common.enums.FundStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Randjith
 * @version 1.0.0
 * @class FundStatusUpdateRequest
 * <p>Represents the input object for updating fund status. It includes properties such as status type.
 * @email "mailto:randjithkumar@no-reply@github.com"
 * @date 2026.07.10
 * @since 0.0.1-SNAPSHOT
 */
@Getter
@Setter
public class FundStatusUpdateRequest {

    /**
     * Represents the fund status being updated.
     * <p>Describes the current status of a financial instrument, such as an asset allocation or trading strategy.</p>
     *
     * @see FundStatus for more details on the different statuses.
     */
    @NotNull
    private FundStatus status;
}
