package associoholic.com.client.common

import associoholic.com.client.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class OkHttp {

    companion object {

        @Volatile
        private var INSTANCE: OkHttpClient? = null

        fun getInstance(): OkHttpClient =
                INSTANCE ?: synchronized(this) {
                    INSTANCE ?: buildOkHttpClient().also { INSTANCE = it }
                }

        private fun buildOkHttpClient(): OkHttpClient {
            val b = OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
            if (BuildConfig.DEBUG) b.addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
            return b.build()
        }
    }

}