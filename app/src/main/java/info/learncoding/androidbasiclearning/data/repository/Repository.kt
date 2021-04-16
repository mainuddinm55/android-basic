package info.learncoding.androidbasiclearning.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import info.learncoding.androidbasiclearning.data.local.AppDatabase
import info.learncoding.androidbasiclearning.data.model.Post
import info.learncoding.androidbasiclearning.data.model.User
import info.learncoding.androidbasiclearning.data.network.Api
import info.learncoding.androidbasiclearning.data.network.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Repository(val app: Application, private val api: Api, private val database: AppDatabase) :
    HandleApiRequest(app) {
    suspend fun fetchRemoteUsers(): ApiResponse<List<User>> {
        return safeApiCall { api.fetchUsers() }
    }

    suspend fun fetchRemotePost(userId: Int?): ApiResponse<List<Post>> {
        return safeApiCall { api.fetchPost(userId) }
    }

    suspend fun post(post: Post): ApiResponse<Post> {
        return safeApiCall { api.post(post) }
    }

    fun fetchLocalUsers(): LiveData<List<User>> {
        return database.userDao().getUsers()
    }

    fun fetchLocalPosts(userId: Int?): LiveData<List<Post>> {
        userId?.let {
            return database.postDao().getPost(userId)
        } ?: kotlin.run {
            return database.postDao().getPost()
        }
    }

    suspend fun insertUsers(users: List<User>) {
        withContext(Dispatchers.IO) {
            database.userDao().insertUser(users)
        }
    }

    suspend fun insertPost(posts: List<Post>) {
        withContext(Dispatchers.IO) {
            database.postDao().insertPost(posts)
        }
    }
}