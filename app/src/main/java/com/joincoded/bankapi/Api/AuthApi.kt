package com.joincoded.bankapi.Api

import com.joincoded.bankapi.dto.AuthenticationRequest
import com.joincoded.bankapi.dto.AuthenticationResponse
import com.joincoded.bankapi.dto.CheckTokenResponse
import com.joincoded.bankapi.dto.RegistrationRequestDTO
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response

interface AuthApi {

    @POST("api/auth/login")
    suspend fun login(
        @Body authRequest: AuthenticationRequest): Response<AuthenticationResponse>

    @POST("api/auth/register")
    suspend fun register(
        @Body request: RegistrationRequestDTO): Response<Any>

    @POST("api/auth/check-token")
    suspend fun checkToken(): Response<CheckTokenResponse>
}