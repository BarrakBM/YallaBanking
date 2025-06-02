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

data class userDTO(
    val userId: Long,
    val name: String
)

data class allUsers(
    val userList: List<userDTO>
)

data class DeAtivateDTO(
    val userId: Long,
    val isActive: Boolean
)


data class userTransactionDTO(
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val time: String // Changed from LocalDate to String
)
// save
data class allTransactionHistoryRespone(
    val transactionHistory: List<allTransactionDTO>
)



// Option 1: Keep as String (if backend sends date as string)
data class allTransactionDTO(
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val type: String,
    val time: String
)

// Option 2: Use a date object if backend sends LocalDate as object
/*
data class LocalDateDTO(
    val year: Int,
    val month: Int,
    val day: Int
)

data class allTransactionDTO(
    val from: String,
    val to: String,
    val amount: BigDecimal,
    val type: String,
    val time: LocalDateDTO
)
*/