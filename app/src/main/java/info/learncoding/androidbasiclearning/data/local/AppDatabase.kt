package info.learncoding.androidbasiclearning.data.local

import android.content.Context
import android.service.autofill.UserData
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import info.learncoding.androidbasiclearning.data.local.dao.PostDao
import info.learncoding.androidbasiclearning.data.local.dao.UserDao
import info.learncoding.androidbasiclearning.data.model.Post
import info.learncoding.androidbasiclearning.data.model.User


@Database(entities = [Post::class, User::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun postDao(): PostDao

    companion object {
        const val DATABASE_NAME = "android_basic"

        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                ).fallbackToDestructiveMigration()
                    .build().also { instance = it }
            }

        }
    }

}
