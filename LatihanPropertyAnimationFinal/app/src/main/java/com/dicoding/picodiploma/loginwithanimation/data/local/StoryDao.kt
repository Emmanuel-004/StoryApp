package com.dicoding.picodiploma.loginwithanimation.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface StoryDao {
    @Query("SELECT * FROM story")
    fun getLiveDataAllStories(): LiveData<List<StoryEntity>>

    @Query("SELECT * FROM story")
    suspend fun getAllStories(): List<StoryEntity>

    @Query("DELETE FROM story")
    suspend fun deleteAll()

}