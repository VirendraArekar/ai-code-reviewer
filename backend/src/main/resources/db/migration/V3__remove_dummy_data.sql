-- ========================================================
-- Migration V3: Clean Initial Dummy Data
-- Wipes seed repositories, pull requests, files, reviews,
-- findings, and notifications so the system strictly analyzes
-- real GitHub repositories and live pull requests.
-- ========================================================

DELETE FROM finding_comments;
DELETE FROM review_findings;
DELETE FROM ai_reviews;
DELETE FROM review_jobs;
DELETE FROM pull_request_files;
DELETE FROM pull_requests;
DELETE FROM repositories;
DELETE FROM notifications;
