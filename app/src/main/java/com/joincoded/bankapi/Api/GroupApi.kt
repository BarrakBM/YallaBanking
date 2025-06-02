package com.joincoded.bankapi.api

import com.joincoded.bankapi.dto.*
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.Response

interface GroupApi {

    @POST("groups/v1/create")
    suspend fun createGroup(
        @Header("Authorization") token: String,
        @Body groupDto: GroupDto
    ): Response<GroupResponseDTO>

    @POST("groups/v1/createWithMembers")
    suspend fun createGroupWithMembers(
        @Header("Authorization") token: String,
        @Body groupDto: CreateGroupRequest
    ): Response<GroupDto>


    @POST("groups/v1/addMember")
    suspend fun addMemberToGroup(
        @Header("Authorization") token: String,
        @Body addMemberRequest: AddGroupMemberRequestDTO
    ): Response<GroupMemberResponseDTO>

    @POST("groups/v1/payment")
    suspend fun payForGroup(
        @Header("Authorization") token: String,
        @Body paymentDTO: GroupPaymentDTO
    ): Response<GroupPaymentResponseDTO>

    @POST("groups/v1/removeMember")
    suspend fun removeGroupMember(
        @Header("Authorization") token: String,
        @Body removeMemberRequest: RemoveGroupMemberRequestDTO
    ): Response<userRemoved>

    @POST("groups/v1/de-activate-group")
    suspend fun deActivateGroup(
        @Header("Authorization") token: String,
        @Body deActivateGroupRequestDTO: deActivateGroupRequestDTO
    ): Response<Any>

    @POST("groups/v1/details")
    suspend fun getGroupDetails(
        @Header("Authorization") token: String,
        @Body groupRequest: GroupIdRequestDTO
    ): Response<GroupDetailsDTO>
}