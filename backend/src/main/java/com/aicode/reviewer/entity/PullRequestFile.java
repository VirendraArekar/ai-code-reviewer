package com.aicode.reviewer.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "pull_request_files")
public class PullRequestFile {

    @Id
    @Column(length = 36)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pull_request_id", nullable = false)
    private PullRequest pullRequest;

    @Column(nullable = false, length = 1024)
    private String filename;

    @Column(name = "old_path", length = 1024)
    private String oldPath;

    @Column(nullable = false, length = 50)
    private String status;

    @Column(nullable = false)
    private int additions = 0;

    @Column(nullable = false)
    private int deletions = 0;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String patch;

    @Column(name = "findings_count", nullable = false)
    private int findingsCount = 0;

    public PullRequestFile() {}

    public PullRequestFile(String id, PullRequest pullRequest, String filename, String oldPath,
                           String status, int additions, int deletions, String patch, int findingsCount) {
        this.id = id;
        this.pullRequest = pullRequest;
        this.filename = filename;
        this.oldPath = oldPath;
        this.status = status;
        this.additions = additions;
        this.deletions = deletions;
        this.patch = patch;
        this.findingsCount = findingsCount;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String id;
        private PullRequest pullRequest;
        private String filename;
        private String oldPath;
        private String status;
        private int additions = 0;
        private int deletions = 0;
        private String patch;
        private int findingsCount = 0;

        public Builder id(String id) { this.id = id; return this; }
        public Builder pullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; return this; }
        public Builder filename(String filename) { this.filename = filename; return this; }
        public Builder oldPath(String oldPath) { this.oldPath = oldPath; return this; }
        public Builder status(String status) { this.status = status; return this; }
        public Builder additions(int additions) { this.additions = additions; return this; }
        public Builder deletions(int deletions) { this.deletions = deletions; return this; }
        public Builder patch(String patch) { this.patch = patch; return this; }
        public Builder findingsCount(int findingsCount) { this.findingsCount = findingsCount; return this; }

        public PullRequestFile build() {
            return new PullRequestFile(id, pullRequest, filename, oldPath, status, additions, deletions, patch, findingsCount);
        }
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public PullRequest getPullRequest() { return pullRequest; }
    public void setPullRequest(PullRequest pullRequest) { this.pullRequest = pullRequest; }
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
