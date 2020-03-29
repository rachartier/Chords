package fr.iut.chords.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class UberChordsClient {
    companion object {
        const val BASE_URL = "https://api.uberchord.com/v1/"
        const val EMBEDDED_URL = "https://api.uberchord.com/v1/embed/chords/"

        val webservice: IUberChordsAPI by lazy {
            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()
                .create(IUberChordsAPI::class.java)
        }
    }
}