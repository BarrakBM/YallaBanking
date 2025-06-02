package com.joincoded.bankapi.api

import com.joincoded.bankapi.dto.TransferInfoDTO
import com.joincoded.bankapi.dto.accountInformationDTO
import com.joincoded.bankapi.dto.fundGroupDTO
import com.joincoded.bankapi.dto.InformationDTO
import com.joincoded.bankapi.dto.allTransactionHistoryRespone
import com.joincoded.bankapi.dto.TransferResponseDTO
import com.joincoded.bankapi.dto.userfundResponse
import com.joincoded.bankapi.dto.DeAtivateDTO
import com.joincoded.bankapi.dto.allUsers
import com.joincoded.bankapi.dto.userDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface AccountApi {

    @POST("account/v1/addOrUpdateInformation")
    suspend fun addInformation(
        @Header("Authorization") token: String,
        @Body accountInfo: accountInformationDTO
    ): Response<accountInformationDTO>

    @GET("account/v1/GetInformation")
    suspend fun viewInformation(
        @Header("Authorization") token: String
    ): Response<InformationDTO>

    @GET("/account/v1/allUsers")
    suspend fun getAllUsers(
        @Header("Authorization") token: String
    ): Response<List<userDTO>>


    @POST("account/v1/deactive")
    suspend fun deactiveAccount(
        @Header("Authorization") token: String
    ): Response<DeAtivateDTO>

    @POST("account/v1/fundGroup")
    suspend fun userFundGroup(
        @Header("Authorization") token: String,
        @Body fundgrp: fundGroupDTO
    ): Response<userfundResponse>

    @GET("account/v1/userTransactionHistory")
    suspend fun userTransactionHistory(
        @Header("Authorization") token: String
    ): Response<allTransactionHistoryRespone>

    @POST("account/v1/transfer")
    suspend fun transferMoney(
        @Header("Authorization") token: String,
        @Body transferInfo: TransferInfoDTO
    ): Response<TransferResponseDTO>
}