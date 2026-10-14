package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.PactEntity
import com.example.data.model.SummonedJinnEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface JinnDao {
    @Query("SELECT * FROM summoned_jinns ORDER BY summonedAt DESC")
    fun getAllSummonedJinns(): Flow<List<SummonedJinnEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSummonedJinn(jinn: SummonedJinnEntity)

    @Query("SELECT * FROM summoned_jinns WHERE id = :id LIMIT 1")
    suspend fun getJinnById(id: String): SummonedJinnEntity?

    @Query("SELECT * FROM sealed_pacts ORDER BY createdAt DESC")
    fun getAllPacts(): Flow<List<PactEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPact(pact: PactEntity)

    @Update
    suspend fun updatePact(pact: PactEntity)

    @Query("DELETE FROM sealed_pacts WHERE id = :id")
    suspend fun deletePact(id: String)
}
