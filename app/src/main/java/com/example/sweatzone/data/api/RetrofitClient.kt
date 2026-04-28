package com.example.sweatzone.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // Physical Device IP (Host PC IP) - Updated based on your network config
    const val BASE_URL = "http://10.208.238.119/SweatZone/"

    // For Android Emulator: "http://10.0.2.2/SweatZone/"

    val api: ApiService by lazy {
        val client = okhttp3.OkHttpClient.Builder()
            .connectTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(30, java.util.concurrent.TimeUnit.SECONDS)
            .addInterceptor(okhttp3.logging.HttpLoggingInterceptor().apply {
                level = okhttp3.logging.HttpLoggingInterceptor.Level.BODY
            })
            .addInterceptor { chain ->
                val original = chain.request()
                val token = com.example.sweatzone.data.local.TokenManager.getToken()
                val requestBuilder = original.newBuilder()
                
                if (!token.isNullOrEmpty()) {
                    requestBuilder.header("Authorization", "Bearer $token")
                }
                
                // Bypass VS Code DevTunnel Warning Page
                requestBuilder.header("X-Tunnel-Skip-Anti-Phishing-Page", "1")
                
                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(com.google.gson.GsonBuilder().setLenient().create()))
            .build()
            .create(ApiService::class.java)
    }
}
