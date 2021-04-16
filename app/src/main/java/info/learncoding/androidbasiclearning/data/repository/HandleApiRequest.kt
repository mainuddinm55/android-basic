package info.learncoding.androidbasiclearning.data.repository

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import info.learncoding.androidbasiclearning.data.network.ApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException

open class HandleApiRequest(val application: Application) {
    suspend fun <T> safeApiCall(apiCall: suspend () -> T): ApiResponse<T> {
        return withContext(Dispatchers.IO) {
            try {
                if (isNetworkAvailable(application)) {
                    ApiResponse.Success(apiCall.invoke())
                } else {
                    ApiResponse.Error("No Internet Connection")
                }
            } catch (e: Throwable) {
                e.printStackTrace()
                when (e) {
                    is IOException -> {
                        ApiResponse.Error("No Internet Connection")
                    }

                    else -> {
                        ApiResponse.Error(e.localizedMessage ?: "Something went wrong")
                    }
                }
            }
        }
    }

    private fun isNetworkAvailable(application: Application): Boolean {
        val connectivityManager =
            application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        var result = false
        connectivityManager.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                it.getNetworkCapabilities(connectivityManager.activeNetwork)?.apply {
                    result = when {
                        hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                        hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                        else -> false
                    }
                }
            } else {
                connectivityManager.activeNetworkInfo?.apply {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }
}