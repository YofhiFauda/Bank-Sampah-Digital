package com.sampah.core.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.*
import com.sampah.core.data.local.entity.SampahEntity
import kotlinx.coroutines.flow.Flow
@Dao
interface SampahDao {
    @Query("SELECT * FROM sampah")
    fun getAllSampah(): Flow<List<SampahEntity>>

    @Query("SELECT * FROM sampah WHERE namaPengguna LIKE '%' || :fullName || '%' ")
    fun searchQuery(fullName: String): LiveData<List<SampahEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertSampah(sampah: List<SampahEntity>)


    @Query("DELETE FROM sampah")
    fun deleteAllSampah()

    @Delete
    fun deleteSampah(sampah: SampahEntity)

    @Query("DELETE FROM sampah WHERE sampah.id = :sampahId")
    fun deleteSampahById(sampahId: Int)

    @Update
    fun updateSampah(sampah: SampahEntity)

    @Query("SELECT * FROM sampah WHERE id = :sampahId")
    fun findSampahById(sampahId: Int): LiveData<List<SampahEntity>>

}