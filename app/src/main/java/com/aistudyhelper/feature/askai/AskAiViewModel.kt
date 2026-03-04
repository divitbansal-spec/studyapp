package com.aistudyhelper.feature.askai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aistudyhelper.domain.AiRequest
import com.aistudyhelper.domain.AiResponse
import com.aistudyhelper.domain.PlanType
import com.aistudyhelper.domain.StudyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class AskAiUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val data: AiResponse? = null,
    val showPaywall: Boolean = false,
)

class AskAiViewModel(
    private val uid: String,
    private val repository: StudyRepository,
) : ViewModel() {

    private val _uiState = MutableStateFlow(AskAiUiState())
    val uiState: StateFlow<AskAiUiState> = _uiState.asStateFlow()

    fun submitQuestion(question: String, subject: String) {
        viewModelScope.launch {
            _uiState.value = AskAiUiState(isLoading = true)
            runCatching {
                val entitlement = repository.getEntitlement(uid)
                val quota = repository.getQuotaStatus(uid)
                val blocked = entitlement.planType == PlanType.FREE && !quota.hasFreeQuota
                if (blocked) {
                    _uiState.value = AskAiUiState(showPaywall = true)
                    return@launch
                }

                val response = repository.solveQuestion(
                    uid = uid,
                    request = AiRequest(inputText = question, subject = subject),
                )
                repository.incrementUsage(uid)
                _uiState.value = AskAiUiState(data = response)
            }.onFailure { throwable ->
                _uiState.value = AskAiUiState(errorMessage = throwable.message ?: "Unknown error")
            }
        }
    }
}
