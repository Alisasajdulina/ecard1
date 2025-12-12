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
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        val dao = AppDatabase.getInstance(application).cardDao()
        repo = CardRepository(dao)

        // observe database with search
        viewModelScope.launch {
            combine(
                searchQuery,
                selectedCategory
            ) { query, category ->
                when {
                    !query.isBlank() -> repo.search(query)
                    !category.isNullOrBlank() -> repo.getByCategory(category)
                    else -> repo.observeAll()
                }
            }.flatMapLatest { it }
                .catch { e ->
                    _uiState.value = UiState.Error(e.localizedMessage ?: "unknown")
                }
                .collect { list -> _uiState.value = UiState.Success(list) }
        }
    }
    
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun setCategory(category: String?) {
        _selectedCategory.value = category
    }
    
    fun clearFilters() {
        _searchQuery.value = ""
        _selectedCategory.value = null
    }
    
    suspend fun getAllCategories(): List<String> = repo.getAllCategories()
    
    suspend fun incrementViewCount(cardId: Long) = repo.incrementViewCount(cardId)

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
