package com.joincoded.bankapi.dto

import java.math.BigDecimal
import java.time.LocalDate


data class accountInformationDTO(

    val name: String,
    val balance: BigDecimal,
    val gender: Int?
)

data class InformationDTO(

    val name: String,
    val balance: BigDecimal,
    val isActive: Boolean,
    val gender: Int
)


data class TransferInfoDTO(
    val destinationId: Long,
    val amount: BigDecimal
)

data class TransferResponseDTO(
    val userId: Long,
    val newBalance: BigDecimal
)


data class userfundResponse(
    val userAmount: BigDecimal,
    val groupAmount: BigDecimal
)

data class fundGroupDTO(
    val groupId: Long,
    val amount: BigDecimal,
    val description: String
)


data class DeAtivateDTO(
    val userId: Long,
    val isActive: Boolean
)


data class userTransactionDTO(
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val time: LocalDate
)
// save
data class allTransactionHistoryRespone(
    val transactionHistory: List<allTransactionDTO>
)



data class allTransactionDTO(
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val type: String,
    val time: LocalDate
)