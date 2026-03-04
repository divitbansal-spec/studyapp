package com.aistudyhelper.feature.scan

import com.aistudyhelper.domain.AiRequest
import com.aistudyhelper.domain.AiResponse
import com.aistudyhelper.domain.OcrRepository
import com.aistudyhelper.domain.StudyRepository

class ScanHomeworkUseCase(
    private val studyRepository: StudyRepository,
    private val ocrRepository: OcrRepository,
) {
    suspend operator fun invoke(
        uid: String,
        imageUri: String,
        subject: String,
    ): AiResponse {
        val extracted = ocrRepository.extractTextFromImage(imageUri)
        return studyRepository.solveQuestion(
            uid = uid,
            request = AiRequest(
                inputText = extracted,
                subject = subject,
                fromScan = true,
            ),
        )
    }
}
