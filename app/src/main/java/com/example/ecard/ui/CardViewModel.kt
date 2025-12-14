package com.example.ecard.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecard.data.AppDatabase
import com.example.ecard.data.CardEntity
import com.example.ecard.data.CardRepository
import com.example.ecard.utils.AuthManager
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
    private val authManager: AuthManager
    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    init {
        val dao = AppDatabase.getInstance(application).cardDao()
        repo = CardRepository(dao)
        authManager = AuthManager(application)

        // observe database with search
        viewModelScope.launch {
<<<<<<< HEAD
            val userId = authManager.getCurrentUserId()
            if (userId != null) {
                combine(
                    searchQuery,
                    selectedCategory
                ) { query, category ->
                    when {
                        !query.isBlank() -> repo.search(query, userId)
                        !category.isNullOrBlank() -> repo.getByCategory(category, userId)
                        else -> repo.observeAll(userId)
                    }
                }.flatMapLatest { it }
                    .catch { e ->
                        _uiState.value = UiState.Error(e.localizedMessage ?: "unknown")
                    }
                    .collect { list -> _uiState.value = UiState.Success(list) }
            } else {
                _uiState.value = UiState.Success(emptyList())
            }
=======
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
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
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
    
<<<<<<< HEAD
    suspend fun getAllCategories(): List<String> {
        val userId = authManager.getCurrentUserId() ?: return emptyList()
        return repo.getAllCategories(userId)
    }
    
    suspend fun incrementViewCount(cardId: Long) {
        val userId = authManager.getCurrentUserId() ?: return
        repo.incrementViewCount(cardId, userId)
    }
=======
    suspend fun getAllCategories(): List<String> = repo.getAllCategories()
    
    suspend fun incrementViewCount(cardId: Long) = repo.incrementViewCount(cardId)
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4

    fun createOrUpdate(card: CardEntity, onResult: (Result<Long>) -> Unit) {
        viewModelScope.launch {
            try {
                val userId = authManager.getCurrentUserId()
                if (userId == null) {
                    onResult(Result.failure(Exception("Пользователь не авторизован")))
                    return@launch
                }
                val id = withContext(Dispatchers.IO) { repo.createOrUpdate(card, userId) }
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
        val userId = authManager.getCurrentUserId() ?: return@withContext null
        repo.getById(id, userId)
    }
}
