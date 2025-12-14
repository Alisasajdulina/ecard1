package com.example.ecard.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {
    @Query("SELECT * FROM cards WHERE userId = :userId ORDER BY updatedAt DESC")
    fun observeAll(userId: Long): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE id = :id AND userId = :userId")
    suspend fun getById(id: Long, userId: Long): CardEntity?

    @Query("SELECT * FROM cards WHERE userId = :userId AND (name LIKE '%' || :query || '%' OR company LIKE '%' || :query || '%' OR phone LIKE '%' || :query || '%' OR email LIKE '%' || :query || '%') ORDER BY updatedAt DESC")
    fun search(query: String, userId: Long): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE category = :category AND userId = :userId ORDER BY updatedAt DESC")
    fun getByCategory(category: String, userId: Long): Flow<List<CardEntity>>

    @Query("SELECT * FROM cards WHERE tags LIKE '%' || :tag || '%' AND userId = :userId ORDER BY updatedAt DESC")
    fun getByTag(tag: String, userId: Long): Flow<List<CardEntity>>

    @Query("SELECT DISTINCT category FROM cards WHERE category IS NOT NULL AND userId = :userId")
    suspend fun getAllCategories(userId: Long): List<String>

    @Query("SELECT * FROM cards WHERE isPrimary = 1 AND userId = :userId LIMIT 1")
    suspend fun getPrimaryCard(userId: Long): CardEntity?

    @Query("UPDATE cards SET viewCount = viewCount + 1, lastViewed = :timestamp WHERE id = :id")
    suspend fun incrementViewCount(id: Long, timestamp: Long)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(card: CardEntity): Long

    @Update
    suspend fun update(card: CardEntity)

    @Delete
    suspend fun delete(card: CardEntity)
}
