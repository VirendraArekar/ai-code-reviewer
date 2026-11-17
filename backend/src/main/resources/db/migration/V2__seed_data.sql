-- ========================================================
-- Seed Data for Development & Testing
-- Password for all seed users is 'AdminPassword123!'
-- BCrypt Hash: $2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy
-- ========================================================

-- Insert Default Organization
INSERT INTO organizations (id, name, slug, plan, owner_id)
VALUES ('org-1', 'Acme Engineering', 'acme-engineering', 'ENTERPRISE', 'u-1')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Insert Default Super Admin and Developers
INSERT INTO users (id, email, password_hash, full_name, role, email_verified, organization_id)
VALUES 
('u-1', 'admin@aicode.com', '$2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy', 'Virendra Arekar', 'SUPER_ADMIN', TRUE, 'org-1'),
('u-2', 'alex@aicode.com', '$2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy', 'Alex Rivera', 'TEAM_LEAD', TRUE, 'org-1'),
('u-3', 'sarah@aicode.com', '$2a$10$d9jEkizqdlDFnMWPWCNS9euqNOBx2fOr683Wa/F1hQWi5VQ/vrCIy', 'Sarah Chen', 'DEVELOPER', TRUE, 'org-1')
ON DUPLICATE KEY UPDATE email=VALUES(email);

-- Insert Organization Members
INSERT INTO organization_members (id, organization_id, user_id, role)
VALUES
('om-1', 'org-1', 'u-1', 'SUPER_ADMIN'),
('om-2', 'org-1', 'u-2', 'TEAM_LEAD'),
('om-3', 'org-1', 'u-3', 'DEVELOPER')
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- Insert Teams
INSERT INTO teams (id, organization_id, name, description)
VALUES
('team-1', 'org-1', 'Backend Platform', 'Core microservices, auth, databases, and API gateways'),
('team-2', 'org-1', 'Frontend & Mobile', 'Web dashboards, mobile apps, and design systems')
ON DUPLICATE KEY UPDATE name=VALUES(name);

-- Insert Team Members
INSERT INTO team_members (id, team_id, user_id, role)
VALUES
('tm-1', 'team-1', 'u-2', 'TEAM_LEAD'),
('tm-2', 'team-1', 'u-3', 'DEVELOPER')
ON DUPLICATE KEY UPDATE role=VALUES(role);

-- Insert AI Provider Configs
INSERT INTO ai_provider_configs (id, organization_id, provider, model_name, is_default, enabled, base_url)
VALUES
('p-1', 'org-1', 'GEMINI', 'gemini-2.0-flash', TRUE, TRUE, 'https://generativelanguage.googleapis.com'),
('p-2', 'org-1', 'OPENAI', 'gpt-4o', FALSE, TRUE, 'https://api.openai.com/v1'),
('p-3', 'org-1', 'OLLAMA', 'deepseek-coder:6.7b', FALSE, TRUE, 'http://localhost:11434')
ON DUPLICATE KEY UPDATE model_name=VALUES(model_name);
