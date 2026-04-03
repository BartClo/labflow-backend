# GitHub Actions Workflows - LabFlow CI/CD

Comprehensive security-first CI/CD pipeline for LabFlow with automated testing, security scanning, and intelligent deployment.

## 📋 Workflows Overview

### 1. **build-backend.yml** - Backend Build & Deployment
Triggered on push/PR to `main` or `develop` with changes in `labflow-backend/`

**Jobs:**
- 🧪 **Test & Quality** - Unit tests, coverage, code quality analysis
- 🔒 **Security Scans** - Credential detection, trivy, SAST (Semgrep), SCA (Dependency-Check)
- 🏗️ **Build & Push Image** - Docker image build and push to GHCR
- 📢 **Deployment Coordination** - Triggers infrastructure deployment

**Outputs:**
- Coverage reports (Codecov)
- Test results
- Docker image on GHCR
- Repository dispatch to infra repo

---

### 2. **build-frontend.yml** - Frontend Build & Deployment
Triggered on push/PR to `main` or `develop` with changes in `labflow-frontend/`

**Jobs:**
- 🔍 **Lint and Test** - ESLint, Prettier, unit tests
- 📦 **Dependency Audit** - npm audit, Snyk, SCA
- 🔒 **Security Scans** - Same security tools as backend
- 🏗️ **Build & Push Image** - Docker build and GHCR push
- 📢 **Deployment Coordination** - Slack notifications, repo dispatch

**Outputs:**
- Linting reports
- Coverage data
- Docker image on GHCR

---

### 3. **security-scan.yml** - Reusable Security Workflow
Called by both backend and frontend workflows via `workflow_call`

**Jobs Included:**
- 🔐 **Detect Secrets** - TruffleHog, git-secrets
- 📦 **SCA - Trivy** - Filesystem and config vulnerabilities
- 🔍 **SAST - Semgrep** - Code pattern & vulnerability scanning
- 📋 **Dependency Check** - Comprehensive dependency analysis
- 🐳 **Container Scan - Trivy** - Docker image vulnerability scanning
- 🎯 **API Security & OpenAPI** - API endpoint security validation
- 📸 **Generate SBOM** - CycloneDX and SPDX formats
- 📊 **Security Summary** - PR comments with results

**Severity Levels:**
- Fails on: CRITICAL vulnerabilities
- Reports: HIGH, MEDIUM as warnings
- Configurable via policy

---

### 4. **deploy.yml** - Infrastructure Deployment
Triggered by:
- `repository_dispatch` events from build workflows (auto-deploy to production on main branch)
- Manual `workflow_dispatch` (test staging deployments)

**Jobs:**
- 🔧 **Prepare Deployment** - Environment detection, deployment ID generation
- 🌐 **Deploy to Staging** - docker-compose.staging.yml
- 🚀 **Deploy to Production** - docker-compose.prod.yml with rollback
- 📢 **Notification** - Slack alerts, GitHub deployment status
- 🧹 **Cleanup** - Old image cleanup

**Deployment Flow:**
```
Backend Build Success (main)
  ↓
Repository Dispatch: deploy-backend
  ↓
Deploy to Production
  ↓
Health Checks
  ↓
Smoke Tests
  ↓
Slack Notification
```

---

### 5. **security-audit.yml** - Scheduled Security Audit
Runs weekly (Monday 2 AM UTC) or on manual trigger

**Jobs:**
- 📚 **Comprehensive Dependency Audit** - Maven + npm audits
- 🎯 **Code Quality & Pattern Scan** - Semgrep comprehensive rules
- 🐳 **Container Image Vulnerability Scan** - Trivy on built images
- 🔐 **Advanced Credential Detection** - TruffleHog + detect-secrets
- 📸 **SBOM Generation & Tracking** - CycloneDX + SPDX
- 📜 **License Compliance Check** - FOSSA, npm license-checker
- 🔒 **API Security & OpenAPI Audit** - API endpoint security
- 📊 **Consolidated Security Report** - GitHub issue creation

---

### 6. **integration-tests.yml** - Quality Assurance
Runs on push/PR to ensure quality gates

**Jobs:**
- 🧪 **Integration Tests** - Full app integration with PostgreSQL
- ⚡ **Performance Tests** - JMeter load testing (main branch only)
- 🎯 **End-to-End Tests** - Playwright browser automation
- ♿ **Accessibility Audit** - pa11y, Accessibility Insights
- 🚪 **Quality Gate Check** - Pass/fail decision logic
- 📋 **Publish Test Reports** - Artifact upload + PR comments

**Minimum Quality Score: 70/100**

---

## 🔐 Required GitHub Secrets

Add these to your repository settings: **Settings → Security → Secrets and variables → Actions**

### Core Secrets
```
GITHUB_TOKEN          # Auto-generated, no setup needed
GHCR_USERNAME         # Your GitHub username
GHCR_TOKEN            # GitHub Personal Access Token (packages:read,write)
```

### Database Credentials
```
DATABASE_URL_STAGING      # jdbc:postgresql://host:5432/labflow_staging
DATABASE_USER_STAGING     # labflow_user
DATABASE_PASSWORD_STAGING # secure_password

DATABASE_URL_PRODUCTION      # jdbc:postgresql://host:5432/labflow
DATABASE_USER_PRODUCTION     # labflow_user
DATABASE_PASSWORD_PRODUCTION # secure_password
POSTGRES_PASSWORD_PRODUCTION # Postgres admin password
```

### Application Secrets
```
JWT_SECRET_PRODUCTION          # Long random string (min 32 chars)
ARGON2_SALT_LENGTH            # Default: 16 (optional)
VITE_API_URL                  # Backend API URL for frontend build
```

### Security & Monitoring
```
SONAR_TOKEN          # SonarCloud token (optional, for code quality)
SNYK_TOKEN           # Snyk token (optional, for npm audit)
FOSSA_API_KEY        # FOSSA license scanning (optional)
SLACK_WEBHOOK        # Slack channel webhook for notifications (optional)
```

### Generate Tokens
1. **GitHub Token**: Settings → Developer settings → Personal access tokens
2. **Slack Webhook**: Create incoming webhook in Slack app directory
3. **SonarCloud**: https://sonarcloud.io → Authentication tokens
4. **Snyk**: https://app.snyk.io → Account settings → API token

---

## 📊 Workflow Triggers

### Automatic (Event-Based)
- **Push to main/develop** → build-backend.yml + build-frontend.yml (if changes detected)
- **Pull requests** → Same builders + integration-tests.yml
- **Monday 2 AM UTC** → security-audit.yml (schedule)
- **Build success on main** → deploy.yml (auto-deploy to production)

### Manual (workflow_dispatch)
- **Actions tab** → Select workflow → "Run workflow"
- Options:
  - **deploy.yml**: Choose environment (staging/production), optionally specify image tags
  - **security-audit.yml**: Immediate security scan
  - **integration-tests.yml**: On-demand quality check

### Repository Dispatch (Cross-Repo Triggers)
Generated automatically by build workflows. Used for:
- Backend build success → Triggers infra deployment
- Frontend build success → Triggers infra deployment

---

## 🚀 First-Time Setup

### 1. Create GitHub Secrets
```bash
# In your GitHub UI, navigate to:
# Settings → Security → Secrets and variables → Actions

# Add all secrets from "Required GitHub Secrets" section above
```

### 2. Verify Dockerfile Paths
Workflows expect these Dockerfile locations:
```
labflow-backend/Dockerfile
labflow-frontend/Dockerfile
```

### 3. Create Directory Structure
```bash
git clone <repo>
mkdir -p .github/workflows
# Workflows are auto-created in this PR
```

### 4. Configure docker-compose Files
Ensure you have:
- `docker-compose.dev.yml` (local development)
- `docker-compose.staging.yml` (staging env)
- `docker-compose.prod.yml` (production env)

### 5. Set Up Database Migrations
The integration tests expect Flyway/Liquibase migrations:
```
labflow-backend/src/main/resources/db/migration/V1__Init.sql
```

### 6. Configure GHCR Registry Access
Enable in repository settings:
- **Settings → Packages → Allow read/write access**

---

## 📈 Monitoring & Observability

### GitHub Security Tab
All SARIF uploads go to: **Security → Code scanning**
- View all vulnerabilities
- Track remediation progress
- Set policies

### GitHub Actions Tab
Monitor each workflow run:
- **Actions** → Select workflow
- View logs, artifacts, timings
- Trigger manual runs

### Status Badges
Add to README.md:
```markdown
![Backend Build](https://github.com/YOUR_ORG/labflow/actions/workflows/build-backend.yml/badge.svg)
![Frontend Build](https://github.com/YOUR_ORG/labflow/actions/workflows/build-frontend.yml/badge.svg)
![Security Audit](https://github.com/YOUR_ORG/labflow/actions/workflows/security-audit.yml/badge.svg)
```

---

## 🛑 Failure Handling

### Build Failures
**build-backend.yml / build-frontend.yml**
- Tests fail → Pipeline stops, PR marked as failing
- Security scan fails → Allows merge (can be configured)
- Docker build fails → No image pushed

**Recovery:**
1. Fix code/tests locally
2. Push to branch
3. Workflow re-runs automatically

### Deployment Failures
**deploy.yml**
- Service health check times out → Automatic rollback
- Database migration fails → Manual recovery needed
- Slack notification sent

**Recovery:**
1. Check deployment logs: **Actions → deploy.yml run**
2. Investigate error (usually in "Health checks" step)
3. Trigger manual re-deploy via workflow_dispatch

### Security Issues
**security-audit.yml**
- Critical vulnerabilities → GitHub issue created automatically
- Does not block deployment (warning only)

**Recovery:**
1. Check GitHub issue with findings
2. Update dependencies
3. Commit fix, push → Automatically re-scans

---

## 🔧 Customization

### Disable Specific Checks
Edit workflow file, find job, add:
```yaml
if: false  # Disable job
```

### Adjust Severity Levels
In security-scan.yml:
```yaml
severity: 'CRITICAL,HIGH,MEDIUM'  # Change threshold
```

### Change Deployment Schedules
Modify cron in workflow file:
```yaml
schedule:
  - cron: '0 2 * * 1'  # Monday 2 AM
```

### Add Additional Tools
Edit security-scan.yml, add new job:
```yaml
  my-scan:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v4
      - name: Run my tool
        run: # your command
```

---

## 📚 Documentation References

- **GitHub Actions**: https://docs.github.com/en/actions
- **Security**: 
  - Trivy: https://aquasecurity.github.io/trivy/
  - Semgrep: https://semgrep.dev/docs/
  - TruffleHog: https://github.com/trufflesecurity/trufflehog
- **Deployment**: https://docs.docker.com/compose/
- **Quality**: https://sonarsource.com/products/sonarcloud/

---

## ⚙️ Environment Variables

### Staging (.env.staging)
```
DATABASE_URL=jdbc:postgresql://localhost:5432/labflow_staging
DATABASE_USER=labflow_user
DATABASE_PASSWORD=***
SPRING_ENV=staging
```

### Production (.env.production)
```
DATABASE_URL=jdbc:postgresql://prod-db:5432/labflow
DATABASE_USER=labflow_user
DATABASE_PASSWORD=***
SPRING_ENV=production
JWT_SECRET=***
```

---

## 🎯 SLA & Timings

| Workflow | Typical Duration | Max Allowed |
|----------|------------------|------------|
| build-backend | 10-15 min | 30 min |
| build-frontend | 5-8 min | 20 min |
| security-scan (reusable) | 5-10 min | 25 min |
| integration-tests | 15-20 min | 40 min |
| deploy.yml (staging) | 5-10 min | 15 min |
| deploy.yml (production) | 10-15 min | 20 min |
| security-audit (weekly) | 20-30 min | 60 min |

**Total CI/CD Cycle**: ~45-60 minutes for full pipeline on main branch

---

## 🐛 Troubleshooting

### Workflow not triggering on push
- Check branch protection rules (require status checks)
- Verify path filters match your changes
- Ensure workflow file is in `.github/workflows/`

### Docker image not found in registry
- Verify GHCR_TOKEN has `packages:write` scope
- Check registry login step in logs
- Confirm image name matches tag syntax

### Database connection timeout
- Verify DATABASE_URL secret is correct
- Check if database service is running
- Review docker-compose networking

### Slack notifications not working
- Verify SLACK_WEBHOOK secret is set
- Test webhook manually: `curl -X POST -H 'Content-type: application/json' --data @payload.json <webhook_url>`

### Credential detection false positives
- Add to `.secrets.baseline` after review
- Run: `detect-secrets audit .secrets.baseline`

---

## 📞 Support

For issues with:
- **Workflows**: Check GitHub Actions logs, review workflow file
- **Security concerns**: Review GitHub Security tab, create GitHub issue
- **Deployment issues**: Check docker-compose logs, review environment secrets
- **Performance**: Monitor job execution times, optimize slow steps

---

**Last Updated**: 2024-01-15
**Version**: 1.0
**Status**: Production Ready ✅
