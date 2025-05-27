package com.joincoded.bankapi.Api

import com.joincoded.bankapi.dto.AddGroupMemberRequestDTO
import com.joincoded.bankapi.dto.GroupDetailsDTO
import com.joincoded.bankapi.dto.GroupDto
import com.joincoded.bankapi.dto.GroupIdRequestDTO
import com.joincoded.bankapi.dto.GroupMemberResponseDTO
import com.joincoded.bankapi.dto.GroupPaymentDTO
import com.joincoded.bankapi.dto.GroupResponseDTO
import com.joincoded.bankapi.dto.RemoveGroupMemberRequestDTO
import com.joincoded.bankapi.dto.deActivateGroupRequestDTO
import com.joincoded.bankapi.dto.userRemoved
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Response
interface GroupApi {

    @POST("groups/v1/create")
    suspend fun createGroup(
        @Body groupDto: GroupDto): Response<GroupResponseDTO>

    @POST("groups/v1/addMember")
    suspend fun addMemberToGroup(
        @Body addMemberRequest: AddGroupMemberRequestDTO): Response<GroupMemberResponseDTO>

    @POST("groups/v1/payment")
    suspend fun payForGroup(
        @Body paymentDTO: GroupPaymentDTO): Response<Any>

    @POST("groups/v1/removeMember")
    suspend fun removeGroupMember(
        @Body removeMemberRequest: RemoveGroupMemberRequestDTO): Response<userRemoved>

    @POST("groups/v1/de-activate-group")
    suspend fun deActivateGroup(
        @Body deActivateGroupRequestDTO: deActivateGroupRequestDTO): Response<Any>

    @POST("groups/v1/details")
    suspend fun getGroupDetails(
        @Body groupRequest: GroupIdRequestDTO): Response<GroupDetailsDTO>

}