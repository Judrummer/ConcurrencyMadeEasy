package com.github.judrummer.concurrencymadeeasy.data

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path

data class RepoEntity(
    @SerializedName("id") val id: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("full_name") val fullName: String = "",
    @SerializedName("description") val description: String? = "",
    @SerializedName("language") val language: String = "",
    @SerializedName("owner") val owner: OwnerEntity = OwnerEntity(),
    @SerializedName("git_url") val gitUrl: String = "",
    @SerializedName("ssh_url") val sshUrl: String = "",
    @SerializedName("stargazers_count") val stargazersCount: Int = 0
)

data class OwnerEntity(
    @SerializedName("id") val id: String = "",
    @SerializedName("login") val login: String = "",
    @SerializedName("avatar_url") val avatarUrl: String = ""
)

data class ContributorEntity(
    @SerializedName("login") val login: String = "",
    @SerializedName("contributions") val contributions: Int = 0,
    @SerializedName("avatar_url") val avatarUrl: String = ""
)

data class UserEntity(
    @SerializedName("id") val id: String = "",
    @SerializedName("login") val login: String = "",
    @SerializedName("name") val name: String = "",
    @SerializedName("avatar_url") val avatarUrl: String = ""
)

interface GithubApi {
    @GET("users/{username}/repos")
    suspend fun rxGetUserRepos(
        @Path("username") username: String
    ): Single<List<RepoEntity>>

    @GET("repos/{owner}/{name}/contributors")
    fun rxGetContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): Single<List<ContributorEntity>>


    @GET("repos/{owner}/{name}/contributors")
    suspend fun getContributors(
        @Path("owner") owner: String,
        @Path("name") name: String
    ): List<ContributorEntity>

    @GET("users/{username}")
    suspend fun getUser(
        @Path("username") username: String
    ): UserEntity

    @GET("users/{username}/repos")
    suspend fun getUserRepos(
        @Path("username") username: String
    ): List<RepoEntity>
}