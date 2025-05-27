package com.joincoded.bankapi.dto


data class AuthenticationRequest(
    val username: String,
    val password: String
)

data class AuthenticationResponse(
    val token: String
)

data class CheckTokenResponse(
    val userId: Long
)

data class RegisterRequest(
    val username: String,
    val password: String
)

data class RegisterFailureResponse(
    val error: AddUserError
)
data class RegistrationRequestDTO(
    val username: String,
    val password: String,
)

data class registorDTO(
    val username: String,
    val userRegisterd: Long
)

enum class AddUserError {
    INVALID_USERNAME, TOO_SHORT_PASSWORD, MAX_ACCOUNT_LIMIT_REACHED
}