package com.example.ecard.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards ORDER BY updatedAt DESC")
    fun observeAll(): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id")
    suspend fun getById(id: Long): CardEntity?

    @Query("SELECT * FROM cards WHERE name LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%' ORDER BY updatedAt DESC")
    fun search(query: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE category = :category ORDER BY updatedAt DESC")
    fun getByCategory(category: String): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE tags LIKE '%' || :tag || '%' ORDER BY updatedAt DESC")
    fun getByTag(tag: String): Flow<List<CardEntity>>

    @Query("SELECT DISTINCT category FROM cards WHERE category IS NOT NULL")
    suspend fun getAllCategories(): List<String>

    @Query("SELECT * FROM cards WHERE isPrimary = 1 LIMIT 1")
    suspend fun getPrimaryCard(): CardEntity?

    @Query("UPDATE cards SET viewCount = viewCount + 1, lastViewed = :timestamp WHERE id = :id")
    suspend fun incrementViewCount(id: Long, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: CardEntity): Long

    @Update
    suspend fun update(card: CardEntity)

    @Delete
    suspend fun delete(card: CardEntity)
}
