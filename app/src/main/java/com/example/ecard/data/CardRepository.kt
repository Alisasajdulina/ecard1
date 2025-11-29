package com.example.ecard.data

import kotlinx.coroutines.flow.Flow

class CardRepository(private val dao: CardDao) {

    fun observeAll(): Flow<List<CardEntity>> = dao.observeAll()

    suspend fun getById(id: Long): CardEntity? = dao.getById(id)

    suspend fun createOrUpdate(card: CardEntity): Long {
        // validate basic fields before saving
        if (card.name.isBlank()) throw IllegalArgumentException("Name is required")
        card.updatedAt = System.currentTimeMillis()
        return if (card.id == 0L) {
            dao.insert(card)
        } else {
            dao.update(card)
            card.id
        }
    }

    suspend fun delete(card: CardEntity) = dao.delete(card)
}
