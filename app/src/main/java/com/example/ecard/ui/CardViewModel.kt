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

class CardViewModel(application: Application) : AndroidViewModel(application) {

    private val repo: CardRepository
    private val authManager: AuthManager

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    // 🔑 ЭТО ГЛАВНОЕ: именно это использует CardListScreen
    val cards: StateFlow<List<CardEntity>>

    init {
        val dao = AppDatabase.getInstance(application).cardDao()
        repo = CardRepository(dao)
        authManager = AuthManager(application)

        cards = combine(
            searchQuery,
            selectedCategory
        ) { query, category ->
            val userId = authManager.getCurrentUserId()
            if (userId == null) {
                flowOf(emptyList())
            } else {
                when {
                    query.isNotBlank() ->
                        repo.search(query, userId)
                    !category.isNullOrBlank() ->
                        repo.getByCategory(category, userId)
                    else ->
                        repo.observeAll(userId)
                }
            }
        }.flatMapLatest { it }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
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

    suspend fun getAllCategories(): List<String> {
        val userId = authManager.getCurrentUserId() ?: return emptyList()
        return repo.getAllCategories(userId)
    }

    suspend fun incrementViewCount(cardId: Long) {
        val userId = authManager.getCurrentUserId() ?: return
        repo.incrementViewCount(cardId, userId)
    }

    fun createOrUpdate(card: CardEntity, onResult: (Result<Long>) -> Unit) {
        viewModelScope.launch {
            try {
                val userId = authManager.getCurrentUserId()
                    ?: return@launch onResult(Result.failure(Exception("Не авторизован")))

                val id = withContext(Dispatchers.IO) {
                    repo.createOrUpdate(card, userId)
                }
                onResult(Result.success(id))
            } catch (t: Throwable) {
                onResult(Result.failure(t))
            }
        }
    }

    fun delete(card: CardEntity, onResult: (Result<Unit>) -> Unit) {
        viewModelScope.launch {
            try {
                withContext(Dispatchers.IO) {
                    repo.delete(card)
                }
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
