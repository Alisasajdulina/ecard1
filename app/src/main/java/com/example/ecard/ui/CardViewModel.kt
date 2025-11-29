package com.example.ecard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecard.data.AppDatabase
import com.example.ecard.data.CardEntity
import com.example.ecard.data.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

sealed class UiState {
    object Loading: UiState()
    data class Success(val cards: List<CardEntity>): UiState()
    data class Error(val message: String): UiState()
}

class CardViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: CardRepository
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        val dao = AppDatabase.getInstance(application).cardDao()
        repo = CardRepository(dao)

        // observe database
        viewModelScope.launch {
            repo.observeAll()
                .catch { e ->
                    _uiState.value = UiState.Error(e.localizedMessage ?: "unknown")
                }
                .collect { list -> _uiState.value = UiState.Success(list) }
        }
    }

    fun createOrUpdate(card: CardEntity, onResult: (Result<Long>) -> Unit) {
        viewModelScope.launch {
            try {
                val id = withContext(Dispatchers.IO) { repo.createOrUpdate(card) }
                onResult(Result.success(id))
            } catch (t: Throwable) {
                onResult(Result.failure(t))
            }
        }
    }

    fun delete(card: CardEntity, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) { repo.delete(card) }
                onResult(Result.success(Unit))
            } catch (t: Throwable) {
                onResult(Result.failure(t))
            }
        }
    }

    suspend fun getById(id: Long): CardEntity? = withContext(Dispatchers.IO) {
        repo.getById(id)
    }
}
