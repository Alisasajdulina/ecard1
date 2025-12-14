package com.example.ecard.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CardRepository(private val dao: CardDao) {

<<<<<<< HEAD
    fun observeAll(userId: Long): Flow<List<CardEntity>> = dao.observeAll(userId)
    
    fun search(query: String, userId: Long): Flow<List<CardEntity>> = dao.search(query, userId)
    
    fun getByCategory(category: String, userId: Long): Flow<List<CardEntity>> = dao.getByCategory(category, userId)
    
    fun getByTag(tag: String, userId: Long): Flow<List<CardEntity>> = dao.getByTag(tag, userId)
    
    suspend fun getAllCategories(userId: Long): List<String> = dao.getAllCategories(userId)
    
    suspend fun getPrimaryCard(userId: Long): CardEntity? = dao.getPrimaryCard(userId)
    
    suspend fun incrementViewCount(id: Long, userId: Long) {
=======
    fun observeAll(): Flow<List<CardEntity>> = dao.observeAll()
    
    fun search(query: String): Flow<List<CardEntity>> = dao.search(query)
    
    fun getByCategory(category: String): Flow<List<CardEntity>> = dao.getByCategory(category)
    
    fun getByTag(tag: String): Flow<List<CardEntity>> = dao.getByTag(tag)
    
    suspend fun getAllCategories(): List<String> = dao.getAllCategories()
    
    suspend fun getPrimaryCard(): CardEntity? = dao.getPrimaryCard()
    
    suspend fun incrementViewCount(id: Long) {
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
        dao.incrementViewCount(id, System.currentTimeMillis())
    }

    suspend fun getById(id: Long, userId: Long): CardEntity? = dao.getById(id, userId)

    suspend fun createOrUpdate(card: CardEntity, userId: Long): Long {
        // validate basic fields before saving
<<<<<<< HEAD
        if (card.name.isBlank()) throw IllegalArgumentException("Имя обязательно для заполнения")
        
        val now = System.currentTimeMillis()
        val cardToSave = if (card.id == 0L) {
            // Новая визитка
            val existingCards = dao.observeAll(userId).first()
            val isFirstCard = existingCards.isEmpty()
            card.copy(
                userId = userId,
                updatedAt = now,
                isPrimary = isFirstCard
            )
=======
        if (card.name.isBlank()) throw IllegalArgumentException("Name is required")
        card.updatedAt = System.currentTimeMillis()
        
        // Если это первая визитка, делаем её основной
        val existingCards = dao.observeAll().first()
        if (existingCards.isEmpty() && card.id == 0L) {
            card.isPrimary = true
        }
        
        return if (card.id == 0L) {
            dao.insert(card)
>>>>>>> e42ed9d4007788e848c2d149ffb1921f84be32d4
        } else {
            // Обновление существующей визитки
            card.copy(
                userId = userId,
                updatedAt = now
            )
        }
        
        return if (cardToSave.id == 0L) {
            dao.insert(cardToSave)
        } else {
            dao.update(cardToSave)
            cardToSave.id
        }
    }

    suspend fun delete(card: CardEntity) = dao.delete(card)
}
