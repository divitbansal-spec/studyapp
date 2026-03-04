package com.aistudyhelper.domain

enum class PlanType { FREE, PREMIUM }

data class Entitlement(
    val planType: PlanType = PlanType.FREE,
    val expiresAtEpochMillis: Long? = null,
)

data class QuotaStatus(
    val usedToday: Int,
    val maxDailyFree: Int = 5,
) {
    val hasFreeQuota: Boolean get() = usedToday < maxDailyFree
}

data class AiRequest(
    val inputText: String,
    val subject: String,
    val grade: String? = null,
    val fromScan: Boolean = false,
)

data class AiResponse(
    val finalAnswer: String,
    val steps: List<String>,
    val simplified: String,
    val practiceQuestions: List<String>,
)
