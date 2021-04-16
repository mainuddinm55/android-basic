package info.learncoding.androidbasiclearning.ui

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import info.learncoding.androidbasiclearning.data.local.AppDatabase
import info.learncoding.androidbasiclearning.data.model.Post
import info.learncoding.androidbasiclearning.data.model.User
import info.learncoding.androidbasiclearning.data.network.Api
import info.learncoding.androidbasiclearning.data.network.ApiResponse
import info.learncoding.androidbasiclearning.data.repository.Repository
import kotlinx.coroutines.launch

class UserViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = Repository(app, Api.invoke(), AppDatabase.getInstance(app))


    val errorMessage: MediatorLiveData<String?> = MediatorLiveData()
    val users = repository.fetchLocalUsers()

    fun posts(userId: Int?) = repository.fetchLocalPosts(userId)

    fun fetchRemoteUser() {
        viewModelScope.launch {
            when (val response = repository.fetchRemoteUsers()) {
                is ApiResponse.Success -> {
                    repository.insertUsers(response.data)
                }
                is ApiResponse.Error -> {
                    errorMessage.value = response.message
                }
            }
        }
    }

    fun fetchRemotePost(userId: Int?) {
        viewModelScope.launch {
            when (val response = repository.fetchRemotePost(userId)) {
                is ApiResponse.Success -> {
                    Log.d("TAG", "fetchRemotePost: ${response.data.size}")
                    Log.d("TAG", "fetchRemotePost: ${response.data}")
                    repository.insertPost(response.data)
                }
                is ApiResponse.Error -> {
                    errorMessage.value = response.message
                }
            }
        }
    }

    fun post(post: Post): LiveData<ApiResponse<Post>> {
        val response = MediatorLiveData<ApiResponse<Post>>()
        viewModelScope.launch {
            response.postValue(repository.post(post))
        }
        return response
    }
}