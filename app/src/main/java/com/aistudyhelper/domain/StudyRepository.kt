package com.aistudyhelper.domain

interface StudyRepository {
    suspend fun getEntitlement(uid: String): Entitlement
    suspend fun getQuotaStatus(uid: String): QuotaStatus
    suspend fun solveQuestion(uid: String, request: AiRequest): AiResponse
    suspend fun incrementUsage(uid: String)
}

interface OcrRepository {
    suspend fun extractTextFromImage(localUri: String): String
}
