package com.example.ecard.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first

class CardRepository(private val dao: CardDao) {

    fun observeAll(userId: Long): Flow<List<CardEntity>> = dao.observeAll(userId)

    fun search(query: String, userId: Long): Flow<List<CardEntity>> = dao.search(query, userId)

    fun getByCategory(category: String, userId: Long): Flow<List<CardEntity>> = dao.getByCategory(category, userId)

    fun getByTag(tag: String, userId: Long): Flow<List<CardEntity>> = dao.getByTag(tag, userId)

    suspend fun getAllCategories(userId: Long): List<String> = dao.getAllCategories(userId)

    suspend fun getPrimaryCard(userId: Long): CardEntity? = dao.getPrimaryCard(userId)

    suspend fun incrementViewCount(id: Long, userId: Long) {
        dao.incrementViewCount(id, userId, System.currentTimeMillis())
    }

    suspend fun getById(id: Long, userId: Long): CardEntity? = dao.getById(id, userId)

    suspend fun createOrUpdate(card: CardEntity, userId: Long): Long {
        // validate basic fields before saving
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

    suspend fun delete(card: CardEntity, userId: Long) = dao.deleteById(card.id, userId)
}