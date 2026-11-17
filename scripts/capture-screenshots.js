import puppeteer from 'puppeteer-core';
import fs from 'fs';
import path from 'path';

const CHROME_PATH = '/Applications/Google Chrome.app/Contents/MacOS/Google Chrome';
const BASE_URL = 'http://localhost:3000';
const API_URL = 'http://localhost:8080/api/v1';
const OUTPUT_DIR = path.resolve('docs/images');

if (!fs.existsSync(OUTPUT_DIR)) {
  fs.mkdirSync(OUTPUT_DIR, { recursive: true });
}

async function getAuthToken() {
  const res = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    body: JSON.stringify({
      email: 'admin@aicode.com',
      password: 'AdminPassword123!',
    }),
  });
  if (!res.ok) {
    throw new Error(`Login failed with HTTP ${res.status}`);
  }
  return res.json();
}

async function capture() {
  console.log('1. Authenticating...');
  const auth = await getAuthToken();
  console.log('Authenticated as:', auth.user.fullName);

  console.log('2. Launching browser...');
  const browser = await puppeteer.launch({
    executablePath: CHROME_PATH,
    headless: true,
    args: ['--no-sandbox', '--disable-setuid-sandbox', '--disable-dev-shm-usage', '--window-size=1440,900'],
  });

  const page = await browser.newPage();
  await page.setViewport({ width: 1440, height: 900, deviceScaleFactor: 2 });

  // Initialize localStorage with session token
  await page.goto(`${BASE_URL}/login`, { waitUntil: 'networkidle0' });
  await page.evaluate((authData) => {
    localStorage.setItem('token', authData.accessToken);
    localStorage.setItem('refreshToken', authData.refreshToken);
    localStorage.setItem('user', JSON.stringify(authData.user));
    localStorage.setItem('theme', 'light');
    document.documentElement.classList.remove('dark');
    document.documentElement.classList.add('light');
  }, auth);

  // List of screens to capture in light mode
  const lightScreens = [
    { name: '01-dashboard-light.png', path: '/dashboard', title: 'Dashboard' },
    { name: '02-repositories-light.png', path: '/repositories', title: 'Repositories' },
    { name: '03-pull-requests-light.png', path: '/pull-requests', title: 'Pull Requests' },
    { name: '04-findings-light.png', path: '/findings', title: 'Review Findings' },
    { name: '05-analytics-light.png', path: '/analytics', title: 'Analytics & Quality Metrics' },
    { name: '06-teams-light.png', path: '/teams', title: 'Engineering Teams' },
    { name: '07-ai-providers-light.png', path: '/settings/ai', title: 'AI Provider Settings' },
    { name: '08-github-integration-light.png', path: '/settings/github', title: 'GitHub App & Webhooks' },
    { name: '09-notifications-light.png', path: '/notifications', title: 'Notifications' },
    { name: '10-audit-logs-light.png', path: '/admin/audit-logs', title: 'Security Audit Logs' },
    { name: '11-organization-light.png', path: '/settings/organization', title: 'Organization Settings' },
    { name: '12-billing-light.png', path: '/settings/billing', title: 'Usage & Plans' },
  ];

  console.log('3. Capturing Light Mode Screens...');
  for (const s of lightScreens) {
    const targetUrl = `${BASE_URL}${s.path}`;
    console.log(`- Capturing ${s.title} (${targetUrl})...`);
    await page.goto(targetUrl, { waitUntil: 'networkidle2', timeout: 15000 }).catch(() => {});
    await page.evaluate(() => {
      localStorage.setItem('theme', 'light');
      document.documentElement.classList.remove('dark');
      document.documentElement.classList.add('light');
    });
    // Wait for animations and data charts to settle
    await new Promise((r) => setTimeout(r, 1200));

    const outPath = path.join(OUTPUT_DIR, s.name);
    await page.screenshot({ path: outPath, fullPage: false });
    console.log(`  ✓ Saved: ${outPath}`);
  }

  // Also capture Pull Request Detail screen for PR #5
  console.log('- Capturing Pull Request Detail Screen...');
  const prDetailUrl = `${BASE_URL}/pull-requests/ed8c4be7-b1fa-42bb-9e62-a758dda844e2`;
  await page.goto(prDetailUrl, { waitUntil: 'networkidle2', timeout: 15000 }).catch(() => {});
  await page.evaluate(() => {
    localStorage.setItem('theme', 'light');
    document.documentElement.classList.remove('dark');
    document.documentElement.classList.add('light');
  });
  await new Promise((r) => setTimeout(r, 1500));
  const prDetailPath = path.join(OUTPUT_DIR, '03b-pull-request-detail-light.png');
  await page.screenshot({ path: prDetailPath, fullPage: false });
  console.log(`  ✓ Saved: ${prDetailPath}`);

  // Dark Mode screen: Only Findings screen as requested: "dark mode scrren shot only one - findings screen"
  console.log('4. Capturing Dark Mode Findings Screen...');
  const darkFindingsUrl = `${BASE_URL}/findings`;
  await page.goto(darkFindingsUrl, { waitUntil: 'networkidle2', timeout: 15000 }).catch(() => {});
  await page.evaluate(() => {
    localStorage.setItem('theme', 'dark');
    document.documentElement.classList.remove('light');
    document.documentElement.classList.add('dark');
  });
  await new Promise((r) => setTimeout(r, 1200));
  const darkOutPath = path.join(OUTPUT_DIR, 'findings-dark.png');
  await page.screenshot({ path: darkOutPath, fullPage: false });
  console.log(`  ✓ Saved Dark Findings: ${darkOutPath}`);

  await browser.close();
  console.log('5. All screenshots successfully captured!');
}

capture().catch((err) => {
  console.error('Screenshot capture failed:', err);
  process.exit(1);
});
