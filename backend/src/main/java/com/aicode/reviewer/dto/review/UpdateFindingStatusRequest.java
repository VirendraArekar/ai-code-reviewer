package com.aicode.reviewer.dto.review;

import com.aicode.reviewer.entity.enums.FindingStatus;
import jakarta.validation.constraints.NotNull;

public class UpdateFindingStatusRequest {
    @NotNull(message = "Status is required")
    private FindingStatus status;
    private String comment;

    public UpdateFindingStatusRequest() {}
    public UpdateFindingStatusRequest(FindingStatus status, String comment) {
        this.status = status;
        this.comment = comment;
    }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private FindingStatus status;
        private String comment;
        public Builder status(FindingStatus status) { this.status = status; return this; }
        public Builder comment(String comment) { this.comment = comment; return this; }
        public UpdateFindingStatusRequest build() { return new UpdateFindingStatusRequest(status, comment); }
    }

    public FindingStatus getStatus() { return status; }
    public void setStatus(FindingStatus status) { this.status = status; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
