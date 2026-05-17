package com.paryavarankavalu.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.paryavarankavalu.ParyavaranApp
import com.paryavarankavalu.data.model.ReportStatus
import com.paryavarankavalu.data.model.WasteReport
import com.paryavarankavalu.data.repository.ReportRepository
import kotlinx.coroutines.launch

class ReportViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: ReportRepository = (application as ParyavaranApp).repository

    val allReports: LiveData<List<WasteReport>> = repository.allReports
    val userProfile = repository.getUserProfile()

    private val _insertResult = MutableLiveData<Long?>()
    val insertResult: LiveData<Long?> = _insertResult

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun submitReport(report: WasteReport) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val id = repository.insertReport(report)
                _insertResult.value = id
            } catch (e: Exception) {
                _errorMessage.value = "Failed to submit report: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun markAsCleaned(reportId: Long) {
        viewModelScope.launch {
            try {
                repository.updateReportStatus(reportId, ReportStatus.CLEANED)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to update status: ${e.message}"
            }
        }
    }

    fun markAsVerified(reportId: Long) {
        viewModelScope.launch {
            repository.updateReportStatus(reportId, ReportStatus.VERIFIED)
        }
    }

    fun markAsInProgress(reportId: Long) {
        viewModelScope.launch {
            repository.updateReportStatus(reportId, ReportStatus.IN_PROGRESS)
        }
    }

    fun deleteReport(report: WasteReport) {
        viewModelScope.launch {
            try {
                repository.deleteReport(report)
            } catch (e: Exception) {
                _errorMessage.value = "Failed to delete report: ${e.message}"
            }
        }
    }

    fun toggleUserRole(currentRole: com.paryavarankavalu.data.model.UserRole) {
        viewModelScope.launch {
            val newRole = if (currentRole == com.paryavarankavalu.data.model.UserRole.USER) {
                com.paryavarankavalu.data.model.UserRole.ADMIN
            } else {
                com.paryavarankavalu.data.model.UserRole.USER
            }
            repository.updateUserRole(newRole)
        }
    }

    fun getPendingReports(): LiveData<List<WasteReport>> =
        repository.getReportsByStatus(ReportStatus.PENDING)

    fun getCleanedReports(): LiveData<List<WasteReport>> =
        repository.getReportsByStatus(ReportStatus.CLEANED)

    fun clearError() { _errorMessage.value = null }
    fun clearInsertResult() { _insertResult.value = null }
}

class ReportViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ReportViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ReportViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
