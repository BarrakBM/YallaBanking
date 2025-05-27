package com.joincoded.bankapi.Api

import com.joincoded.bankapi.dto.TransferInfoDTO
import com.joincoded.bankapi.dto.accountInformationDTO
import com.joincoded.bankapi.dto.fundGroupDTO
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @POST("account/v1/addOrUpdateInformation")
    suspend fun addInformation(@Body accountInfo: accountInformationDTO): Response<Any>

    @GET("account/v1/GetInformation")
    suspend fun viewInformation(): Response<Any>

    @POST("account/v1/deactive")
    suspend fun deactiveAccount(): Response<Any>

    @POST("account/v1/fundGroup")
    suspend fun userFundGroup(@Body fundgrp: fundGroupDTO): Response<Any>

    @GET("account/v1/userTransactionHistory")
    suspend fun userTransactionHistory(): Response<Any>

    @POST("account/v1/transfer")
    suspend fun transferMoney(@Body transferInfo: TransferInfoDTO): Response<Any>
}