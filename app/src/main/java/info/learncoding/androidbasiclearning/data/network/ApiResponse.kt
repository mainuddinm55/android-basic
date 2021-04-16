package info.learncoding.androidbasiclearning.data.network

sealed class ApiResponse<out T> {
    data class Success<T>(var data: T) : ApiResponse<T>()
    data class Error(var message:String):ApiResponse<Nothing>()
}
