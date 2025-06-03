package com.joincoded.bankapi.dto

import java.math.BigDecimal
import java.time.LocalDate

data class GroupDto(
    val name: String,
    val initialBalance: BigDecimal
)

data class CreateGroupRequest(
    val name: String,
    val description: String? = null,
    val memberIds: List<String>
)


data class GroupResponseDTO(
    val groupId: Long,
    val name: String,
    val balance: BigDecimal
)


data class AddGroupMemberRequestDTO(
    val groupId: Long,
    val userIdToAdd: Long
)

data class GroupMemberResponseDTO(
    val id: Long?,
    val userId: Long,
    val groupId: Long,
    val isAdmin: Boolean,
    val joinedAt: LocalDate
)

// Add this to your GroupDto.kt file
data class RemoveGroupMemberRequestDTO(
    val groupId: Long,
    val userIdToRemove: Long
)

data class userRemoved(
    val groupId: Long,
    val RemovedUserId: Long

)


//--------------------------------


data class FundGroupRequestDTO(
    val groupId: Long,

    )
data class deActivateGroupRequestDTO(
    val groupId: Long,

    )


//for group details info
data class GroupDetailsDTO(
    val groupId: Long,
    val groupName: String,
    val balance: BigDecimal,
    val adminId: Long,
    val adminName: String, // Add admin name
    val members: List<MemberDTO>
)

// Updated MemberDTO to include user name
data class MemberDTO(
    val userId: Long,
    val userName: String, // Add user name
    val isAdmin: Boolean
)

data class GroupIdRequestDTO(
    val groupId: Long
)