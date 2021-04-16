package info.learncoding.androidbasiclearning.data.network

import info.learncoding.androidbasiclearning.data.model.Post
import info.learncoding.androidbasiclearning.data.model.User
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface Api {
    @GET("users")
    suspend fun fetchUsers(): List<User>

    @GET("posts")
    suspend fun fetchPost(@Query("userId") userId: Int?): List<Post>

    @POST("posts")
    suspend fun post(@Body post: Post): Post

    companion object {
        private const val BASE_URL = "https://jsonplaceholder.typicode.com/"
        operator fun invoke(
        ): Api {
            val okHttpClient = OkHttpClient.Builder()
                .callTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(Api::class.java)
        }
    }
}