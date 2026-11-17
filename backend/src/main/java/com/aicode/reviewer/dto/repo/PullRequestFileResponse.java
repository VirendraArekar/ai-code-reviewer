package com.aicode.reviewer.dto.repo;

public class PullRequestFileResponse {
    private String id;
    private String pullRequestId;
    private String filename;
    private String oldPath;
    private String status;
    private int additions;
    private int deletions;
    private String patch;
    private int findingsCount;

    public PullRequestFileResponse() {}

    public static Builder builder() { return new Builder(); }
    public static class Builder {
        private String id;
        private String pullRequestId;
        private String filename;
        private String oldPath;
        private String status;
        private int additions;
        private int deletions;
        private String patch;
        private int findingsCount;

        public Builder id(String id) { this.id = id; return this; }
        public Builder pullRequestId(String pullRequestId) { this.pullRequestId = pullRequestId; return this; }
        public Builder filename(String filename) { this.filename = filename; return this; }
        public Builder oldPath(String oldPath) { this.oldPath = oldPath; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder additions(int additions) { this.additions = additions; return this; }
        public Builder deletions(int deletions) { this.deletions = deletions; return this; }
        public Builder patch(String patch) { this.patch = patch; return this; }
        public Builder findingsCount(int findingsCount) { this.findingsCount = findingsCount; return this; }

        public PullRequestFileResponse build() {
            PullRequestFileResponse r = new PullRequestFileResponse();
            r.id = id; r.pullRequestId = pullRequestId; r.filename = filename;
            r.oldPath = oldPath; r.status = status; r.additions = additions;
            r.deletions = deletions; r.patch = patch; r.findingsCount = findingsCount;
            return r;
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPullRequestId() { return pullRequestId; }
    public void setPullRequestId(String pullRequestId) { this.pullRequestId = pullRequestId; }
    public String getFilename() { return filename; }
    public void setFilename(String filename) { this.filename = filename; }
    public String getOldPath() { return oldPath; }
    public void setOldPath(String oldPath) { this.oldPath = oldPath; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public int getAdditions() { return additions; }
    public void setAdditions(int additions) { this.additions = additions; }
    public int getDeletions() { return deletions; }
    public void setDeletions(int deletions) { this.deletions = deletions; }
    public String getPatch() { return patch; }
    public void setPatch(String patch) { this.patch = patch; }
    public int getFindingsCount() { return findingsCount; }
    public void setFindingsCount(int findingsCount) { this.findingsCount = findingsCount; }
}
