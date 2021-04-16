package info.learncoding.androidbasiclearning.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "post")
data class Post(
    var userId: Int,
    @PrimaryKey
    var id: Int,
    var title: String,
    var body: String
) {
    override fun toString(): String {
        return title + "\n\n" + body
    }
}
