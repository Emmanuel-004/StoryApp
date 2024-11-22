package com.dicoding.picodiploma.loginwithanimation.data

import com.dicoding.picodiploma.loginwithanimation.data.local.StoryDao
import com.dicoding.picodiploma.loginwithanimation.data.local.StoryEntity
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.AddNewStoryResponse
import com.dicoding.picodiploma.loginwithanimation.data.remote.response.ListStoryItem
import com.dicoding.picodiploma.loginwithanimation.data.remote.retrofit.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(
    private val apiService: ApiService,
    private val storyDao: StoryDao
){
    suspend fun getAllStories(token: String): Result<List<StoryEntity>>{
        return try {
            val response = apiService.getStories(token)
            val storyList = response.body()?.listStory?.map { listStoryItem ->
                listStoryItem.let {
                    StoryEntity(
                        id = it.id,
                        name = it.name,
                        description = it.description,
                        photoUrl = it.photoUrl,
                        createdAt = it.createdAt,
                        lat = it.lat as Double,
                        lon = it.lon as Double
                    )
                }
            } ?: emptyList()
            if (storyList.isEmpty()) {
                Result.Error("No stories available")
            } else {
                Result.Success(storyList)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    suspend fun getLocalStories(): List<StoryEntity> {
        return storyDao.getAllStories()
    }

    suspend fun uploadStory(file: MultipartBody.Part, description: RequestBody, token: String): Result<AddNewStoryResponse> {
        return try {
            val response = apiService.uploadImage(file, description, "Bearer $token")
            Result.Success(response)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown error occurred")
        }
    }

    companion object {
        @Volatile
        private var instance : StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
            storyDao: StoryDao
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService, storyDao)
            }. also { instance = it }
    }
}