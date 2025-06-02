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

import android.util.Log

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

    var selectedGroup: GroupDetailsDTO? by mutableStateOf(null)

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

    fun getGroupDetails(groupId: Long) {
        authToken?.let { token ->
            viewModelScope.launch {
                try {
                    val request = GroupIdRequestDTO(groupId)
                    val response = groupApiService.getGroupDetails("Bearer $token", request)

                    if (response.isSuccessful) {
                        selectedGroup = response.body()
                    } else {
                        errorMessage = "Failed to fetch group details"
                    }
                } catch (e: Exception) {
                    errorMessage = "Network error: ${e.message}"
                    Log.e("BankViewModel", "Error fetching group", e)
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


//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.joincoded.bankapi.api.AccountApi
//import com.joincoded.bankapi.api.AuthApi
//import com.joincoded.bankapi.api.GroupApi
//import com.joincoded.bankapi.dto.AuthenticationRequest
//import com.joincoded.bankapi.dto.GroupDetailsDTO
//import com.joincoded.bankapi.dto.InformationDTO
//import com.joincoded.bankapi.dto.RegistrationRequestDTO
//import com.joincoded.bankapi.dto.allTransactionDTO
//import com.joincoded.bankapi.network.RetrofitHelper
//
//
//import kotlinx.coroutines.launch
//
//class BankViewModel : ViewModel() {
//
//
//    //create a service to talk to authentication in backend (port 8081)
//    // include login register etc...
//    private val authApiService = RetrofitHelper.getAuthInstance().create(AuthApi::class.java)
//
//    //create a service to talk to banking in backend (port 8080)
//    // it will handle accounts
//    private val accountApiService = RetrofitHelper.getBankInstance().create(AccountApi::class.java)
//
//    //create a service to talk to banking in backend (port 8080)
//    private val groupApiService = RetrofitHelper.getBankInstance().create(GroupApi::class.java)
//
//    //after successful login, it will store the jwt token
//    // this token will be sent with every Api request
//    var authToken: String? by mutableStateOf(null)
//
//    // to store current userId from the database
//    // retrieved by validating the token with the auth service
//    var currentUserId: Long? by mutableStateOf(null)
//
//    // to indicate if user is currently logged in
//    var isLoggedIn: Boolean by mutableStateOf(false)
//
//
//    // this to store user account info (name, balance, active ..)
//    var userAccount: InformationDTO? by mutableStateOf(null)
//
//    // this to list all user's transaction history
//    var transactionHistory: List<allTransactionDTO> by mutableStateOf(emptyList())
//
//
//    // list of groups where user is a member, and user to display group card
//    var userGroups: List<GroupDetailsDTO> by mutableStateOf(emptyList())
//
//
//    //UI state variables:
//
//    // true when there's a request
//    var isLoading: Boolean by mutableStateOf(false)
//
//    // error message to display to user
//    var errorMessage: String? by mutableStateOf(null)
//
//
//    // success message for example (transfer complete or login successful
//    var successMessage: String? by mutableStateOf(null)
//
//    fun login(username: String, password: String) {
//
//        // running code in background (it won't freeze)
//        viewModelScope.launch {
//
//            isLoading = true
//            errorMessage = null
//
//            try {
//
//                // make a network request to authentication service
//                // create an authentioction request with username/password
//                // send post request to the endpoint
//                val response = authApiService.login(AuthenticationRequest(username, password))
//
//                // check if loggin successful
//                if (response.isSuccessful) {
//                    // if login is successfull it will return token
//                    response.body()?.let { authResponse ->
//                        // store the token
//                        authToken = authResponse.token
//
//                        // update the login status
//                        isLoggedIn = true
//
//                        // validate the token to get user Id
//                        checkToken()
//
//                        // get user account info
//                        loadUserAccount()
//
//                        // success message
//                        successMessage = "Login successful!"
//                    }
//                } else {
//                    // if login faild something wrong with username or password
//                    errorMessage = "Login failed: Invalid credentials"
//                }
//
//            } catch (e: Exception) {
//                // network error (timeout or server down)
//                errorMessage = "Network error: ${e.message}"
//            } finally {
//                // remove loading spinner
//                isLoading = false
//            }
//        }
//    }
//
//
//    fun register(username: String, password: String) {
//        viewModelScope.launch {
//            isLoading = true          // Show loading spinner
//            errorMessage = null       // Clear any previous error messages
//
//            try {
//
//
//                // make network request to authentication service
//                // create a dto and send a post request
//                val response = authApiService.register(RegistrationRequestDTO(username, password))
//
//                // check if regestration is successful
//                if (response.isSuccessful) {
//                    successMessage = "Registration successful!"
//
//
//                } else {
//                    // failed registration
//                    errorMessage = "Registration failed"
//                }
//
//            } catch (e: Exception) {
//                // network error (no internet, server down, timeout, etc.)
//                errorMessage = "Network error: ${e.message}"
//            } finally {
//
//                isLoading = false
//            }
//        }
//    }
//
//    private fun checkToken() {
//        // only proceed if we have a token
//        authToken?.let { token ->
//            viewModelScope.launch {
//                try {
//
//                    // send token validation request to authentication service
//                    val response = authApiService.checkToken("Bearer $token")
//
//                    if (response.isSuccessful) {
//                        // if token is valid extract user Id
//                        currentUserId = response.body()?.userId
//                    } else {
//                        // Token is invalid or expired - log user out
//                        logout()
//                    }
//                } catch (e: Exception) {
//                    // Network error or token validation failed - log user out
//                    logout()
//                }
//            }
//        }
//    }
//
//    fun logout() {
//        // clear auth data
//        authToken = null
//        currentUserId = null
//        isLoggedIn = false
//
//        // clear all banking data
//        userAccount = null
//        transactionHistory = emptyList()
//        userGroups = emptyList()
//
//
//        successMessage = "Logged out successfully"
//    }
//
//    fun loadUserAccount() {
//        // proceed if user has token
//        authToken?.let { token ->
//            viewModelScope.launch {
//
//                isLoading = true
//                errorMessage = null
//
//                try {
//
//                    // request account info from tha banking service
//                    val response = accountApiService.viewInformation("Bearer $token")
//
//                    // check response
//                    if (response.isSuccessful) {
//                        // account found: store the info
//                        userAccount = response.body()
//
//                    } else if (response.code() == 404) {
//                        // User doesn't have an account yet (new user)
//                        userAccount = null
//                        errorMessage = "No account found. Please create an account."
//
//                    } else {
//                        // server error, permission denied, etc...
//                        errorMessage = "Failed to load account information"
//                    }
//
//                } catch (e: Exception) {
//                    // network error
//                    errorMessage = "Network error: ${e.message}"
//                } finally {
//                    isLoading = false
//                }
//            }
//        }
//    }
//
////
////}
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.setValue
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.joincoded.bankapi.api.AccountApi
//import com.joincoded.bankapi.api.AuthApi
//import com.joincoded.bankapi.api.GroupApi
//import com.joincoded.bankapi.dto.AuthenticationRequest
//import com.joincoded.bankapi.dto.GroupDetailsDTO
//import com.joincoded.bankapi.dto.InformationDTO
//import com.joincoded.bankapi.dto.RegistrationRequestDTO
//import com.joincoded.bankapi.dto.accountInformationDTO
//import com.joincoded.bankapi.dto.allTransactionDTO
//import com.joincoded.bankapi.network.RetrofitHelper
//
//
//import kotlinx.coroutines.launch
//import java.math.BigDecimal
//
//class BankViewModel : ViewModel() {
//
//
//    //create a service to talk to authentication in backend (port 8081)
//    // include login register etc...
//    private val authApiService = RetrofitHelper.getAuthInstance().create(AuthApi::class.java)
//
//    //create a service to talk to banking in backend (port 8080)
//    // it will handle accounts
//    private val accountApiService = RetrofitHelper.getBankInstance().create(AccountApi::class.java)
//
//    //create a service to talk to banking in backend (port 8080)
//    private val groupApiService = RetrofitHelper.getBankInstance().create(GroupApi::class.java)
//
//    //after successful login, it will store the jwt token
//    // this token will be sent with every Api request
//    var authToken: String? by mutableStateOf(null)
//
//
//
//    // to store current userId from the database
//    // retrieved by validating the token with the auth service
//    var currentUserId: Long? by mutableStateOf(null)
//
//    // to indicate if user is currently logged in
//    var isLoggedIn: Boolean by mutableStateOf(false)
//    var needSignUp: Boolean by mutableStateOf(false)
//
//
//    // this to store user account info (name, balance, active ..)
//    var userAccount: InformationDTO? by mutableStateOf(null)
//
//    // this to list all user's transaction history
//    var transactionHistory: List<allTransactionDTO> by mutableStateOf(emptyList())
//
//
//    // list of groups where user is a member, and user to display group card
//    var userGroups: List<GroupDetailsDTO> by mutableStateOf(emptyList())
//
//
//    //UI state variables:
//
//    // true when there's a request
//    var isLoading: Boolean by mutableStateOf(false)
//
//    // error message to display to user
//    var errorMessage: String? by mutableStateOf(null)
//
//
//    // success message for example (transfer complete or login successful
//    var successMessage: String? by mutableStateOf(null)
//
//    fun login(username: String, password: String) {
//
//        // running code in background (it won't freeze)
//        viewModelScope.launch {
//
//            isLoading = true
//            errorMessage = null
//
//            try {
//
//                // make a network request to authentication service
//                // create an authentioction request with username/password
//                // send post request to the endpoint
//                val response = authApiService.login(AuthenticationRequest(username, password))
//
//                // check if loggin successful
//                if (response.isSuccessful) {
//                    // if login is successfull it will return token
//                    response.body()?.let { authResponse ->
//                        // store the token
//                        authToken = authResponse.token
//
//                        // update the login status
//                        isLoggedIn = true
//
//                        // validate the token to get user Id
//                        checkToken()
//
//                        val account = accountApiService.viewInformation("Bearer $authToken")
//                        if (account.isSuccessful)
//                        {needSignUp = false}
//                        else {
//                            needSignUp = true
//                        }
//
//
//                        // get user account info
//                        loadUserAccount()
//
//                        // success message
//                        successMessage = "Login successful!"
//                    }
//                } else {
//                    // if login faild something wrong with username or password
//                    errorMessage = "Login failed: Invalid credentials"
//                }
//
//            } catch (e: Exception) {
//                // network error (timeout or server down)
//                errorMessage = "Network error: ${e.message}"
//            } finally {
//                // remove loading spinner
//                isLoading = false
//            }
//        }
//    }
//
//
//    fun register(username: String, password: String) {
//        viewModelScope.launch {
//            isLoading = true          // Show loading spinner
//            errorMessage = null       // Clear any previous error messages
//
//            try {
//
//
//                // make network request to authentication service
//                // create a dto and send a post request
//                val response = authApiService.register(RegistrationRequestDTO(username, password))
//
//                // check if regestration is successful
//                if (response.isSuccessful) {
//                    successMessage = "Registration successful!"
//
//
//                } else {
//                    // failed registration
//                    errorMessage = "Registration failed"
//                }
//
//            } catch (e: Exception) {
//                // network error (no internet, server down, timeout, etc.)
//                errorMessage = "Network error: ${e.message}"
//            } finally {
//
//                isLoading = false
//            }
//        }
//    }
//
//    private fun checkToken() {
//        // only proceed if we have a token
//        authToken?.let { token ->
//            viewModelScope.launch {
//                try {
//
//                    // send token validation request to authentication service
//                    val response = authApiService.checkToken("Bearer $token")
//
//                    if (response.isSuccessful) {
//                        // if token is valid extract user Id
//                        currentUserId = response.body()?.userId
////                        if (response.body().NeedRegistor)
//                    } else {
//                        // Token is invalid or expired - log user out
//                        logout()
//                    }
//                } catch (e: Exception) {
//                    // Network error or token validation failed - log user out
//                    logout()
//                }
//            }
//        }
//    }
//
//    fun logout() {
//        // clear auth data
//        authToken = null
//        currentUserId = null
//        isLoggedIn = false
//
//        // clear all banking data
//        userAccount = null
//        transactionHistory = emptyList()
//        userGroups = emptyList()
//
//
//        successMessage = "Logged out successfully"
//    }
//
//    fun loadUserAccount() {
//        // proceed if user has token
//        authToken?.let { token ->
//            viewModelScope.launch {
//
//                isLoading = true
//                errorMessage = null
//
//                try {
//
//                    // request account info from tha banking service
//                    val response = accountApiService.viewInformation("Bearer $token")
//
//                    // check response
//                    if (response.isSuccessful) {
//                        // account found: store the info
//                        userAccount = response.body()
//
//                    } else if (response.code() == 404) {
//                        // User doesn't have an account yet (new user)
//                        userAccount = null
//                        errorMessage = "No account found. Please create an account."
//
//                    } else {
//                        // server error, permission denied, etc...
//                        errorMessage = "Failed to load account information"
//                    }
//
//                } catch (e: Exception) {
//                    // network error
//                    errorMessage = "Network error: ${e.message}"
//                } finally {
//                    isLoading = false
//                }
//            }
//        }
//    }
//
//    fun createAccount(name: String, balance: BigDecimal, gender: Int?) {
//        val token = authToken
//        if (token == null) {
//            errorMessage = "User is not authenticated. Please log in first."
//            return
//        }
//
//        viewModelScope.launch {
//            isLoading = true
//            errorMessage = null
//            successMessage = null
//
//            try {
//                val dto = accountInformationDTO(
//                    name = name,
//                    balance = balance,
//                    gender = gender
//                )
//
//                val response = accountApiService.addInformation("Bearer $token", dto)
//
//                if (response.isSuccessful) {
//                    userAccount = response.body()
//                    successMessage = "Account created successfully!"
//                    needSignUp = false
//                } else {
//                    errorMessage = "Account creation failed: ${response.message()}"
//                }
//
//            } catch (e: Exception) {
//                errorMessage = "Network error: ${e.message}"
//            } finally {
//                isLoading = false
//            }
//        }
//    }
//
//
//
//
//
//}