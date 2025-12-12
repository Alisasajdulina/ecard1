package com.example.ecard.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CardRepository(private val dao: CardDao) {

    fun observeAll(): Flow<List<CardEntity>> = dao.observeAll()
    
    fun search(query: String): Flow<List<CardEntity>> = dao.search(query)
    
    fun getByCategory(category: String): Flow<List<CardEntity>> = dao.getByCategory(category)
    
    fun getByTag(tag: String): Flow<List<CardEntity>> = dao.getByTag(tag)
    
    suspend fun getAllCategories(): List<String> = dao.getAllCategories()
    
    suspend fun getPrimaryCard(): CardEntity? = dao.getPrimaryCard()
    
    suspend fun incrementViewCount(id: Long) {
        dao.incrementViewCount(id, System.currentTimeMillis())
    }

    suspend fun getById(id: Long): CardEntity? = dao.getById(id)

    suspend fun createOrUpdate(card: CardEntity): Long {
        // validate basic fields before saving
        if (card.name.isBlank()) throw IllegalArgumentException("Name is required")
        card.updatedAt = System.currentTimeMillis()
        
        // Если это первая визитка, делаем её основной
        val existingCards = dao.observeAll().first()
        if (existingCards.isEmpty() && card.id == 0L) {
            card.isPrimary = true
        }
        
        return if (card.id == 0L) {
            dao.insert(card)
        } else {
            dao.update(card)
            card.id
        }
    }

    suspend fun delete(card: CardEntity) = dao.delete(card)
}
