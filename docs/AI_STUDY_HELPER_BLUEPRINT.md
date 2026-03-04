# AI Study Helper — Production Blueprint

## 1) Complete App Architecture

### Tech Stack
- **Client:** Kotlin, Jetpack Compose, Coroutines, Flow, Hilt, Navigation Compose
- **Architecture:** Clean + MVVM + Repository pattern
- **Backend:** Firebase (Auth, Firestore, Cloud Functions, Storage, Remote Config, Analytics, Crashlytics)
- **AI Gateway:** Cloud Functions proxy to model provider (OpenAI/Gemini-compatible API) with policy filters
- **OCR:** ML Kit Text Recognition (on-device first; Cloud Vision fallback for difficult scans)
- **Monetization:** Google Play Billing v6+, AdMob (banner + rewarded)

### Modules (recommended)
- `app` → shell, navigation, DI, theming
- `core:ui` → reusable components and design system
- `core:data` → network/Firebase/Billing/OCR implementations
- `core:domain` → use cases, entities, policies
- `feature:askai`
- `feature:scan`
- `feature:summarize`
- `feature:practice`
- `feature:profile`
- `feature:paywall`

### High-level Data Flow
1. UI sends intent/event to ViewModel
2. ViewModel calls UseCase
3. UseCase validates quota + safety policy + subject routing
4. Repository calls:
   - OCR service (if image)
   - AI proxy endpoint
   - Firestore for persistence/history
5. ViewModel emits state (loading/success/error)
6. Analytics events are logged at key moments (query_start/query_success/paywall_view/subscription_purchase)

### Security & Compliance
- Google Sign-In + Firebase Auth
- API keys never embedded in app (Cloud Functions only)
- Firestore rules scoped by UID
- COPPA-friendly stance: no manipulative claims, clear educational disclaimer
- Abuse prevention: rate limits, prompt filtering, anti-cheat wording ("learning support")

---

## 2) Feature Flow

## Home
- Entry cards: Ask AI, Scan Homework, Summarize Notes, Practice Quiz, Streak, Upgrade
- Daily quota indicator (e.g., `2/5 free questions left`)

## Ask AI
1. User types/pastes question
2. Optional subject + grade selection
3. System checks free quota/premium
4. AI response sections:
   - Final answer
   - Step-by-step solution
   - Simplified explanation
   - 3 practice questions
5. Save to history + allow "Regenerate easier explanation"

## Scan Homework
1. Camera capture / gallery upload
2. OCR extraction + preview for edits
3. Submit to Ask AI pipeline
4. Show structured answer cards

## Summarize Notes
1. Paste long text
2. Select summary mode: Short / Bullet / Exam Revision
3. AI outputs summary, key concepts, likely exam questions
4. Save as flashcard set

## Practice Quiz
1. Pick subject + difficulty + question count
2. AI generates quiz
3. Timed mode (optional)
4. Score + explanations + suggested revision links

## Profile + Premium
- Streak, total solved, saved notes
- Plan: Free/Premium status
- Manage subscription + restore purchases

---

## 3) UI Screen Breakdown (Compose)

### HomeScreen
- Greeting + streak chip + quota chip
- Feature grid cards
- "Continue learning" recent activity carousel

### AskAiScreen
- Prompt box + subject chips + send button
- Response in collapsible cards (`Final`, `Steps`, `Simple`, `Practice`)
- CTA row: Save / Share / Ask Follow-up

### ScanScreen
- Camera preview + capture button
- OCR result editable text area
- Submit to AI button

### SummarizeScreen
- Large text input + mode segmented control
- Output tabs: Summary / Bullets / Key Concepts / Revision Notes

### PracticeScreen
- Setup panel + quiz runner + result summary
- Confetti + streak update when completed

### PremiumScreen
- Value proposition bullets
- Monthly/yearly pricing cards
- FAQ + restore purchase + terms/privacy

---

## 4) Firebase Database Structure (Firestore)

```text
users/{uid}
  profile:
    name, email, grade, targetExam, createdAt
  entitlements:
    plan: "free" | "premium"
    expiryAt
    source: "play_billing"
  usage:
    dailyQuestionCount
    lastQuestionDate
    totalQuestions
    streakDays

users/{uid}/history/{historyId}
  type: "ask" | "scan" | "summarize" | "practice"
  subject
  prompt
  extractedText
  aiResponse:
    finalAnswer
    steps[]
    simplified
    practice[]
  createdAt

users/{uid}/saved/{itemId}
  title
  content
  tags[]
  createdAt

quizzes/{quizId}
  uid
  subject
  difficulty
  questions[]
  score
  createdAt
```

### Cloud Functions
- `aiSolveQuestion` → checks auth, quota, moderation, calls LLM
- `generateQuiz` → structured JSON questions
- `summarizeNotes` → safe concise output
- `onPurchaseValidated` → updates entitlements

---

## 5) Monetization Implementation

### Free Plan
- 5 AI requests/day
- Ads enabled (home banner + rewarded continue for extra question)
- Standard model + standard latency

### Premium Plan (₹199–₹499/month)
- Unlimited requests
- Ad-free
- Advanced reasoning templates
- Priority queue / faster endpoint
- Full practice mode + deeper analytics

### Billing Strategy
- Base plan monthly + intro trial (3 or 7 days)
- Offer tags for A/B tests via Remote Config
- Server-side purchase verification + acknowledgement
- Smart paywall triggers:
  - After hitting 80% free quota
  - After valuable outcome (high intent)

### Ads Strategy
- Frequency cap for interstitials
- Rewarded ad for +2 daily questions (free tier)
- No ads in learning flow where cognitive load is high

---

## 6) Code Snippets (key patterns)

- See Kotlin files under `app/src/main/java/com/aistudyhelper/...` in this repo for:
  - ViewModel state management
  - Quota gating and premium checks
  - OCR-to-AI pipeline contract
  - Billing entitlement handling

---

## 7) Google Play Store Listing

## App Title
**AI Study Helper – Homework Solver & AI Tutor**

## Short Description (80 chars)
Solve homework with AI, learn step-by-step, scan questions, and practice smarter.

## Full Description (draft)
AI Study Helper is your personal AI tutor for daily homework, revision, and exam prep.

### What you can do
- Ask any homework question and get step-by-step solutions
- Scan worksheet photos and get instant explanations
- Summarize long notes into quick revision points
- Generate practice quizzes by subject and difficulty
- Save study history and track your learning streak

### Built for real learning
AI Study Helper explains concepts in simple language so you understand *why* an answer works, not just what the answer is.

### Free vs Premium
**Free:** 5 AI questions/day + ads
**Premium:** unlimited questions, ad-free experience, faster responses, advanced explanations, and full quiz mode

Perfect for students studying Math, Science, Coding, and more.

Disclaimer: AI responses are educational support and should be verified with your curriculum materials.

## Keywords
- AI homework solver
- study helper
- AI tutor
- homework AI
- study assistant

### App Icon Concept
- Rounded-square icon with graduation cap + spark/brain motif
- Colors: Indigo + Cyan gradient (trust + tech + youth)
- Minimal silhouette for readability at small sizes

### Screenshot Layout (7 images)
1. **Hero:** "Your AI Tutor for Every Homework Question"
2. Ask AI step-by-step answer card
3. Scan homework via camera flow
4. Notes summarizer before/after
5. Practice quiz + score insights
6. Streak/history dashboard
7. Premium benefits comparison

---

## 8) Launch Strategy

### Phase 1 (Weeks 1–2): Pre-launch
- Closed testing with 100–300 students
- Track activation funnel: Install → Sign-in → First solved question
- Fix OCR edge cases (handwriting/low-light)

### Phase 2 (Weeks 3–4): Soft launch (India Tier-1 + Tier-2)
- Paid acquisition: YouTube Shorts + Instagram Reels creative
- ASO A/B test screenshots + subtitle variants
- Monitor D1 retention, paywall conversion, crash-free rate

### Phase 3 (Weeks 5+): Scale
- Add local-language support (Hindi + Hinglish)
- Introduce exam-specific packs (CBSE/ICSE/JEE/NEET starter prompts)
- Referral loops: "Invite friend for 3 premium days"

### KPI Targets
- D1 retention: >35%
- D7 retention: >15%
- Trial-to-paid: 8–12%
- Crash-free sessions: >99.5%
- Avg response latency: <6s for standard questions

---

## Play Compliance Checklist
- Clear subscription pricing + renewal terms
- In-app access to Terms, Privacy, and account deletion path
- Ad labels and family-safe ad categories
- No deceptive performance claims
- Educational disclaimer for AI-generated outputs
