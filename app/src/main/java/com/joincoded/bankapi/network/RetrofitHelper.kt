package com.joincoded.bankapi.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    // Authentication microservice (Port 8081)
    private const val AUTH_BASE_URL = "http://10.0.2.2:8083/"  // for Android emulator
    //private const val AUTH_BASE_URL = "http://192.168.1.x:8081/"  // phone

    // Banking microservice (Port 8080)
    private const val BANK_BASE_URL = "http://10.0.2.2:8084/"  // Android emulator
    //private const val BANK_BASE_URL = "http://192.168.1.x:8082/"  // phone

    fun getAuthInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(AUTH_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getBankInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BANK_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }


    fun getInstance(): Retrofit {
        return getAuthInstance()
    }
}