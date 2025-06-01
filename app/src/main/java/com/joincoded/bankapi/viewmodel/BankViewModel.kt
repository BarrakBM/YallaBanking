package com.joincoded.bankapi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.joincoded.bankapi.api.AccountApi
import com.joincoded.bankapi.api.AuthApi
import com.joincoded.bankapi.api.GroupApi
import com.joincoded.bankapi.dto.*
import com.joincoded.bankapi.network.RetrofitHelper
import kotlinx.coroutines.launch
import java.math.BigDecimal

class BankViewModel : ViewModel() {

    // API services
    private val authApiService = RetrofitHelper.getAuthInstance().create(AuthApi::class.java)
    private val accountApiService = RetrofitHelper.getBankInstance().create(AccountApi::class.java)
    private val groupApiService = RetrofitHelper.getBankInstance().create(GroupApi::class.java)

    // Authentication state
    var authToken: String? by mutableStateOf(null)
    var currentUserId: Long? by mutableStateOf(null)
    var isLoggedIn: Boolean by mutableStateOf(false)
    var needSignUp: Boolean by mutableStateOf(false)

    // User data
    var userAccount: InformationDTO? by mutableStateOf(null)
    var transactionHistory: List<allTransactionDTO> by mutableStateOf(emptyList())
    var userGroups: List<GroupDetailsDTO> by mutableStateOf(emptyList())

    // UI state
    var isLoading: Boolean by mutableStateOf(false)
    var errorMessage: String? by mutableStateOf(null)
    var successMessage: String? by mutableStateOf(null)

    // Transfer state
    var transferAmount: String by mutableStateOf("")
    var destinationId: String by mutableStateOf("")

    fun login(username: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = authApiService.login(AuthenticationRequest(username, password))

                if (response.isSuccessful) {
                    response.body()?.let { authResponse ->
                        authToken = authResponse.token
                        isLoggedIn = true

                        checkToken()

                        // Check if user needs to create account profile
                        val account = accountApiService.viewInformation("Bearer $authToken")
                        needSignUp = !account.isSuccessful

                        if (!needSignUp) {
                            loadUserAccount()
                            loadTransactionHistory()
                            loadUserGroups()
                        }

                        successMessage = "Login successful!"
                    }
                } else {
                    errorMessage = "Login failed: Invalid credentials"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun register(username: String, password: String) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null

            try {
                val response = authApiService.register(RegistrationRequestDTO(username, password))

                if (response.isSuccessful) {
                    successMessage = "Registration successful! Please login."
                } else {
                    errorMessage = "Registration failed"
                }
            } catch (e: Exception) {
                errorMessage = "Network error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    fun createAccount(accountInfo: accountInformationDTO) {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val response = accountApiService.addInformation("Bearer $token", accountInfo)

                    if (response.isSuccessful) {
                        needSignUp = false
                        loadUserAccount()
                        loadTransactionHistory()
                        successMessage = "Account created successfully!"
                    } else {
                        errorMessage = "Failed to create account"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun transferMoney(destinationId: Long, amount: BigDecimal) {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val transferInfo = TransferInfoDTO(destinationId, amount)
                    val response = accountApiService.transferMoney("Bearer $token", transferInfo)

                    if (response.isSuccessful) {
                        loadUserAccount() // Refresh balance
                        loadTransactionHistory() // Refresh transaction history
                        successMessage = "Transfer completed successfully!"
                        transferAmount = ""
                        this@BankViewModel.destinationId = ""
                    } else {
                        errorMessage = "Transfer failed"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun createGroup(groupDto: GroupDto) {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val response = groupApiService.createGroup("Bearer $token", groupDto)

                    if (response.isSuccessful) {
                        loadUserGroups() // Refresh groups list
                        successMessage = "Group created successfully!"
                    } else {
                        errorMessage = "Failed to create group"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun addGroupMember(groupId: Long, userIdToAdd: Long) {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val addMemberRequest = AddGroupMemberRequestDTO(groupId, userIdToAdd)
                    val response = groupApiService.addMemberToGroup("Bearer $token", addMemberRequest)

                    if (response.isSuccessful) {
                        loadUserGroups() // Refresh groups
                        successMessage = "Member added successfully!"
                    } else {
                        errorMessage = "Failed to add member"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun fundGroup(groupId: Long, amount: BigDecimal, description: String = "Group funding") {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val fundGroupRequest = fundGroupDTO(groupId, amount, description)
                    val response = accountApiService.userFundGroup("Bearer $token", fundGroupRequest)

                    if (response.isSuccessful) {
                        loadUserAccount() // Refresh balance
                        loadTransactionHistory() // Refresh transactions
                        loadUserGroups() // Refresh groups
                        successMessage = "Group funded successfully!"
                    } else {
                        errorMessage = "Failed to fund group"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    private fun checkToken() {
        authToken?.let { token ->
            viewModelScope.launch {
                try {
                    val response = authApiService.checkToken("Bearer $token")

                    if (response.isSuccessful) {
                        currentUserId = response.body()?.userId
                    } else {
                        logout()
                    }
                } catch (e: Exception) {
                    logout()
                }
            }
        }
    }

    fun loadUserAccount() {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val response = accountApiService.viewInformation("Bearer $token")

                    if (response.isSuccessful) {
                        userAccount = response.body()
                    } else if (response.code() == 404) {
                        userAccount = null
                        errorMessage = "No account found. Please create an account."
                    } else {
                        errorMessage = "Failed to load account information"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }

    fun loadTransactionHistory() {
        authToken?.let { token ->
            viewModelScope.launch {
                try {
                    val response = accountApiService.userTransactionHistory("Bearer $token")

                    if (response.isSuccessful) {
                        transactionHistory = response.body()?.transactionHistory ?: emptyList()
                    }
                } catch (e: Exception) {
                    // Silent fail for transaction history
                }
            }
        }
    }

    private fun loadUserGroups() {
        // For now, we'll leave this empty since we need a specific endpoint
        // to get user's groups. You can add this when the backend endpoint is ready
        authToken?.let { token ->
            viewModelScope.launch {
                try {
                    // TODO: Implement when backend has getUserGroups endpoint
                    // val response = groupApiService.getUserGroups("Bearer $token")
                    // if (response.isSuccessful) {
                    //     userGroups = response.body() ?: emptyList()
                    // }
                    userGroups = emptyList() // Placeholder
                } catch (e: Exception) {
                    // Silent fail for groups loading
                    userGroups = emptyList()
                }
            }
        }
    }

    fun logout() {
        authToken = null
        currentUserId = null
        isLoggedIn = false
        needSignUp = false
        userAccount = null
        transactionHistory = emptyList()
        userGroups = emptyList()
        transferAmount = ""
        destinationId = ""
        successMessage = "Logged out successfully"
    }

    fun clearMessages() {
        errorMessage = null
        successMessage = null
    }

    fun deactivateAccount() {
        authToken?.let { token ->
            viewModelScope.launch {
                isLoading = true
                errorMessage = null

                try {
                    val response = accountApiService.deactiveAccount("Bearer $token")

                    if (response.isSuccessful) {
                        logout()
                        successMessage = "Account deactivated successfully"
                    } else {
                        errorMessage = "Failed to deactivate account"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                } finally {
                    isLoading = false
                }
            }
        }
    }
}