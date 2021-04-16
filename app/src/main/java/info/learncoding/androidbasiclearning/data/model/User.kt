package info.learncoding.androidbasiclearning.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    var id: Int,
    var name: String,
    var email: String
) {
    override fun toString(): String {
        return name + "\n" + email
    }
}