package info.learncoding.androidbasiclearning.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import info.learncoding.androidbasiclearning.data.model.Post

@Dao
interface PostDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(posts: List<Post>)

    @Query("SELECT * FROM post WHERE userId=:userId")
    fun getPost(userId: Int?): LiveData<List<Post>>

    @Query("SELECT * FROM post")
    fun getPost(): LiveData<List<Post>>
}