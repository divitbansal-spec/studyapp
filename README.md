# AI Study Helper

Production blueprint and Android starter architecture for **AI Study Helper – Homework Solver & AI Tutor**.

## Contents
- Product, UX, architecture, monetization, ASO, launch strategy:
  - `docs/AI_STUDY_HELPER_BLUEPRINT.md`
- Kotlin architecture snippets:
  - `app/src/main/java/com/aistudyhelper/domain/Models.kt`
  - `app/src/main/java/com/aistudyhelper/domain/StudyRepository.kt`
  - `app/src/main/java/com/aistudyhelper/feature/askai/AskAiViewModel.kt`
  - `app/src/main/java/com/aistudyhelper/feature/scan/ScanHomeworkUseCase.kt`
  - `app/src/main/java/com/aistudyhelper/feature/subscription/BillingEntitlementMapper.kt`

## Notes
This repository currently contains the production design, implementation blueprint, and core code patterns to bootstrap the full app implementation.

## GitHub Pages Troubleshooting
If GitHub Pages still shows 404 after these files exist, verify:
1. **Settings → Pages → Source** points to the same branch you are deploying.
2. Source folder is either **/(root)** (uses `index.html`) or **/docs** (uses `docs/index.html`).
3. Wait 1–3 minutes after pushing for Pages to rebuild.
