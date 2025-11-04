
package com.example.ecardnarwhal.data

import kotlinx.coroutines.flow.Flow

class CardRepository(private val dao: CardDao) {
    fun getAll(): Flow<List<CardEntity>> = dao.getAll()
    suspend fun insert(card: CardEntity) = dao.insert(card)
    suspend fun getById(id: Long) = dao.getById(id)
    suspend fun delete(card: CardEntity) = dao.delete(card)
}
