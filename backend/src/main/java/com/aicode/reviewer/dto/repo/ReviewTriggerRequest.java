package com.aicode.reviewer.dto.repo;

import com.aicode.reviewer.entity.enums.ReviewMode;

public class ReviewTriggerRequest {
    private ReviewMode mode;

    public ReviewTriggerRequest() {}
    public ReviewTriggerRequest(ReviewMode mode) { this.mode = mode; }

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private ReviewMode mode;
        public Builder mode(ReviewMode mode) { this.mode = mode; return this; }
        public ReviewTriggerRequest build() { return new ReviewTriggerRequest(mode); }
    }

    public ReviewMode getMode() { return mode; }
    public void setMode(ReviewMode mode) { this.mode = mode; }
}
