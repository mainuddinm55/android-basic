package info.learncoding.androidbasiclearning.data.local.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import info.learncoding.androidbasiclearning.data.model.User

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(users: List<User>)

    @Query("SELECT * FROM user")
    fun getUsers(): LiveData<List<User>>
}