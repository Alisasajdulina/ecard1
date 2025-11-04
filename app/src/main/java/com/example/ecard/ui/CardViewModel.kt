
package com.example.ecardnarwhal.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecard.data.AppDatabase
import com.example.ecardnarwhal.data.CardEntity
import com.example.ecardnarwhal.data.CardRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class CardViewModel(app: Application): AndroidViewModel(app) {
    private val repo = CardRepository(AppDatabase.get(app).cardDao())
    val cards = repo.getAll().stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    fun add(card: CardEntity) = viewModelScope.launch { repo.insert(card) }
    fun getById(id: Long, block: (CardEntity?)->Unit) = viewModelScope.launch {
        block(repo.getById(id))
    }
    fun delete(card: CardEntity) = viewModelScope.launch { repo.delete(card) }
}
